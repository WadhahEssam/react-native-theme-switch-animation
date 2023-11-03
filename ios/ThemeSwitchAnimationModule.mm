
#import <UIKit/UIKit.h>

#import "ThemeSwitchAnimationModule.h"


@implementation ThemeSwitchAnimationModule
{
    UIImageView *overlayView;
    BOOL isAnimating;
}

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    return @[@"FINISHED_FREEZING_SCREEN"];
}

RCT_EXPORT_METHOD(freezeScreen)
{
    if (!isAnimating) {
        dispatch_async(dispatch_get_main_queue(), ^{
            UIImage *capturedScreen = [self captureScreen];
            self->isAnimating = YES;
            [self displayCapturedImageFullScreen:capturedScreen];
            [self triggerEvent];
        });
    }
}

- (void)triggerEvent {
    [self sendEventWithName:@"FINISHED_FREEZING_SCREEN" body:@{@"key": @"value"}];
}

RCT_EXPORT_METHOD(unfreezeScreen: (NSString*) type duration:(NSInteger) duration csRation:(CGFloat) cxRatio cyRation:(CGFloat) cyRatio)
{
    if (isAnimating) {
        void (^completionCallback)(void) = ^{
            NSLog(@"Animation Completed");
            self->isAnimating = false;
        };
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self performInvertedCircleAnimation: self->overlayView duration:duration cxRatio:cxRatio cyRatio:cyRatio callback: completionCallback];
        });
    }
}

- (void)displayCapturedImageFullScreen:(UIImage *)image {
    overlayView = [[UIImageView alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    overlayView.image = image;
    overlayView.contentMode = UIViewContentModeScaleAspectFill;
    overlayView.tag = 100;  // optional, if you want to remove it later by tag
    
    [[UIApplication sharedApplication].keyWindow addSubview:overlayView];
}

- (UIImage *)captureScreen {
    UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
    CGRect rect = [keyWindow bounds];
    UIGraphicsBeginImageContextWithOptions(rect.size,YES,0.0f);
    CGContextRef context = UIGraphicsGetCurrentContext();
    [keyWindow.layer renderInContext:context];
    UIImage *capturedScreen = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return capturedScreen;
}

- (void)performFadeAnimation: (NSInteger) duration {
    [UIView animateWithDuration: duration / 1000
                     animations:^{
        self->overlayView.alpha = 0.0;
    }
                     completion:^(BOOL finished){
        self->isAnimating = NO;
        [self->overlayView removeFromSuperview];
    }];
}

- (void)performInvertedCircleAnimation:(UIView *)overlayView
                     duration:(CFTimeInterval)duration
                      cxRatio:(CGFloat)cxRatio
                      cyRatio:(CGFloat)cyRatio
                     callback:(void (^)(void))callback {
    dispatch_async(dispatch_get_main_queue(), ^{
        CGFloat width = CGRectGetWidth(overlayView.bounds);
        CGFloat height = CGRectGetHeight(overlayView.bounds);
        
        NSLog(@"%f", width);
        NSLog(@"%f", height);
        NSLog(@"%f", cxRatio);
        NSLog(@"%f", cyRatio);
        CGPoint center = CGPointMake(width * cxRatio, height * cyRatio);
        CGFloat startRadius = [self getPointMaxDistanceInsideContainerWithCx:center.x cy:center.y width:width height:height];
        


        NSLog(@"start radius %f", startRadius);
        UIBezierPath *startPath = [UIBezierPath bezierPathWithArcCenter:center
                                                                 radius:startRadius
                                                             startAngle:0
                                                               endAngle:M_PI * 2
                                                              clockwise:YES];
        
        // Create a circular path that acts as the end state of the animation
        UIBezierPath *endPath = [UIBezierPath bezierPathWithArcCenter:center
                                                               radius:0.1
                                                           startAngle:0
                                                             endAngle:M_PI * 2
                                                            clockwise:YES];
        
        
        CAShapeLayer *maskLayer = [CAShapeLayer layer];
        maskLayer.path = startPath.CGPath;
        overlayView.layer.mask = maskLayer;
        
        CABasicAnimation *maskLayerAnimation = [CABasicAnimation animationWithKeyPath:@"path"];
        maskLayerAnimation.fromValue = (__bridge id)(startPath.CGPath);
        maskLayerAnimation.toValue = (__bridge id)(endPath.CGPath);

        maskLayerAnimation.duration = duration / 1000;
        maskLayerAnimation.delegate = self;
        maskLayerAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
        maskLayerAnimation.fillMode = kCAFillModeForwards;
        maskLayerAnimation.removedOnCompletion = NO;
        
        
        [CATransaction begin];
        [CATransaction setCompletionBlock:^{
            overlayView.layer.mask = nil;
            overlayView.hidden = YES;
            if (callback) {
                callback();
            }
            maskLayerAnimation.delegate = nil; // Set delegate to nil to prevent memory leak
            [self->overlayView removeFromSuperview];

        }];
        
        [maskLayer addAnimation:maskLayerAnimation forKey:@"path"];
        [CATransaction commit];
    });
}

- (CGFloat)getPointMaxDistanceInsideContainerWithCx:(CGFloat)cx cy:(CGFloat)cy width:(CGFloat)width height:(CGFloat)height {
    CGFloat topLeftDistance = hypotf(cx, cy);
    CGFloat topRightDistance = hypotf(width - cx, cy);
    CGFloat bottomLeftDistance = hypotf(cx, height - cy);
    CGFloat bottomRightDistance = hypotf(width - cx, height - cy);
    return MAX(MAX(topLeftDistance, topRightDistance), MAX(bottomLeftDistance, bottomRightDistance));
}

@end
