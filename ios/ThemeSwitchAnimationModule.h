
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface ThemeSwitchAnimationModule : RCTEventEmitter <RCTBridgeModule, CAAnimationDelegate>

@property (nonatomic, strong) UIImageView *overlayView;
@property (nonatomic, assign) BOOL isAnimating;

- (void)captureAndDisplayScreen;
- (void)displayCapturedImageFullScreen:(UIImage *)image;
- (UIImage *)captureScreen;

@end
