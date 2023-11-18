#ifdef RCT_NEW_ARCH_ENABLED

#import "RNThemeSwitchAnimationModule.h"
#import <React/RCTEventEmitter.h>

@interface ThemeSwitchAnimationModule : RCTEventEmitter <NativeThemeSwitchAnimationModuleSpec, CAAnimationDelegate>

@property (nonatomic, strong) UIImageView *overlayView;
@property (nonatomic, assign) BOOL isAnimating;

- (void)captureAndDisplayScreen;
- (void)displayCapturedImageFullScreen:(UIImage *)image;
- (UIImage *)captureScreen;

#else
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface ThemeSwitchAnimationModule : RCTEventEmitter <RCTBridgeModule, CAAnimationDelegate>

@property (nonatomic, strong) UIImageView *overlayView;
@property (nonatomic, assign) BOOL isAnimating;

- (void)captureAndDisplayScreen;
- (void)displayCapturedImageFullScreen:(UIImage *)image;
- (UIImage *)captureScreen;
#endif

@end



