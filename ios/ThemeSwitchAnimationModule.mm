
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
        [self displayCapturedImageFullScreen:capturedScreen];
        
        // Check for listeners before sending the event
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

    // Add Gesture to remove fullscreen view when tapped
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(removeFullScreenImage:)];
    [fullScreenImageView addGestureRecognizer:tapGesture];
    fullScreenImageView.userInteractionEnabled = YES;

    [[UIApplication sharedApplication].keyWindow addSubview:fullScreenImageView];
}

- (void)removeFullScreenImage:(UITapGestureRecognizer *)gesture {
    UIView *view = [gesture view];
    [view removeFromSuperview];
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
        self->isAnimating = NO;
    });
  }
}
//
//- (void)performFadeAnimationOnView:(UIView *)view {
//  isAnimating = YES;
//
//  CABasicAnimation *fadeOutAnimation = [CABasicAnimation animationWithKeyPath:@"opacity"];
//  [fadeOutAnimation setFromValue:[NSNumber numberWithFloat:1.0]];
//  [fadeOutAnimation setToValue:[NSNumber numberWithFloat:0.0]];
//  [fadeOutAnimation setDuration:1.0];
//
//  [fadeOutAnimation setCompletion:^(BOOL finished) {
//    if (finished) {
//      view.hidden = YES;
//      isAnimating = NO;
//    }
//  }];
//
//  [view.layer addAnimation:fadeOutAnimation forKey:@"opacity"];
//}

@end
