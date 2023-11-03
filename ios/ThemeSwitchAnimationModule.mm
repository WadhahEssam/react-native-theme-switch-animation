
#import <UIKit/UIKit.h>

#import "ThemeSwitchAnimationModule.h"


@implementation ThemeSwitchAnimationModule
{
    UIView *overlayView;
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

RCT_EXPORT_METHOD(unfreezeScreen: (NSString*) type duration:(NSInteger) duration csRation:(double) cxRatio cyRation:(double) cyRatio)
{
    NSLog(@"%@", type);
    NSLog(@"%ld",(long) duration);
    NSLog(@"%ld",(long) cyRatio);
    if (isAnimating) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self removeFullScreenImage: duration];
        });
    }
}

- (void)displayCapturedImageFullScreen:(UIImage *)image {
    UIImageView *fullScreenImageView = [[UIImageView alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    fullScreenImageView.image = image;
    fullScreenImageView.contentMode = UIViewContentModeScaleAspectFill;
    fullScreenImageView.tag = 100;  // optional, if you want to remove it later by tag
    overlayView = fullScreenImageView;
    
    [[UIApplication sharedApplication].keyWindow addSubview:fullScreenImageView];
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

- (void)removeFullScreenImage: (NSInteger) duration {
    [UIView animateWithDuration: duration / 1000
                     animations:^{
        self->overlayView.alpha = 0.0;
    }
                     completion:^(BOOL finished){
        self->isAnimating = NO;
        [self->overlayView removeFromSuperview];
    }];
}

@end
