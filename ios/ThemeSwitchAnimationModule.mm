
#import <UIKit/UIKit.h>

#import "ThemeSwitchAnimationModule.h"
#import "AnimationHelper.h"


@implementation ThemeSwitchAnimationModule {
    bool hasListeners;
}


@synthesize overlayView, isAnimating;

static NSString * const kFinishedFreezingScreenEvent = @"FINISHED_FREEZING_SCREEN";

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    return @[@"FINISHED_FREEZING_SCREEN"];
}

RCT_EXPORT_METHOD(freezeScreen)
{
    if (!isAnimating) {
        dispatch_async(dispatch_get_main_queue(), ^{
            self->isAnimating = YES;
            [self captureAndDisplayScreen];
            [self triggerEvent];
        });
    }
}

+ (BOOL)requiresMainQueueSetup {
  return NO; // Return YES if you need to execute on the main thread
}

- (void)startObserving
{
  hasListeners = YES;
}

- (void)stopObserving
{
  hasListeners = NO;
}

- (void)triggerEvent
{
  if (hasListeners) {
      [self sendEventWithName:kFinishedFreezingScreenEvent body:@{@"key": @"value"}];
  }
}

RCT_EXPORT_METHOD(unfreezeScreen: (NSString *)type duration:(double)duration cxRatio:(double)cxRatio cyRatio:(double)cyRatio)
{
    if (isAnimating) {
        void (^completionCallback)(void) = ^{
            self->isAnimating = false;
            self->overlayView.layer.mask = nil;
            self->overlayView.hidden = YES;
            [self->overlayView removeFromSuperview];
        };
        
        dispatch_async(dispatch_get_main_queue(), ^{
            if ([type isEqualToString:@"inverted-circular"]) {
                [AnimationHelper performInvertedCircleAnimation:self->overlayView source:self duration:duration cxRatio:cxRatio cyRatio:cyRatio callback:completionCallback];
            } else if ([type isEqualToString:@"circular"]) {
                [AnimationHelper performCircularAnimation:self->overlayView source:self duration:duration cxRatio:cxRatio cyRatio:cyRatio callback:completionCallback];
            } else {
                [AnimationHelper performFadeAnimation:self->overlayView duration:duration callback:completionCallback];
            }
        });
    }
}

- (void)captureAndDisplayScreen {
    UIImage *capturedScreen = [self captureScreen];
    [self displayCapturedImageFullScreen:capturedScreen];
}

- (void)displayCapturedImageFullScreen:(UIImage *)image {
    overlayView = [[UIImageView alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    overlayView.image = image;
    overlayView.contentMode = UIViewContentModeScaleAspectFill;
    [[UIApplication sharedApplication].keyWindow addSubview:overlayView];
}

- (UIImage *)captureScreen {
    CGSize screenSize = UIScreen.mainScreen.bounds.size;
    UIGraphicsBeginImageContextWithOptions(screenSize, NO, 0.0f);

    // iterating over every window that the application might be using to ensure that layers like modals are captured
    for (UIWindow *window in [UIApplication sharedApplication].windows) {
        if (!window.isHidden && window.alpha > 0) {
            [window drawViewHierarchyInRect:window.bounds afterScreenUpdates:YES];
        }
    }

    UIImage *capturedScreen = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

    return capturedScreen;
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeThemeSwitchAnimationModuleSpecJSI>(params);
}
#endif

@end
