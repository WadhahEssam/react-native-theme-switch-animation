
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

RCT_EXPORT_METHOD(unfreezeScreen)
{
    if (isAnimating) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self removeFullScreenImage];
        });
    }
}

- (void)removeFullScreenImage {
    [UIView animateWithDuration:0.5
                     animations:^{
        self->overlayView.alpha = 0.0;
    }
                     completion:^(BOOL finished){
        self->isAnimating = NO;
        [self->overlayView removeFromSuperview];
    }];
}

@end
