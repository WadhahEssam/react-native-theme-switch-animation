#import <React/RCTEventEmitter.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import "RNThemeSwitchAnimationModule.h"
@interface ThemeSwitchAnimationModule : RCTEventEmitter <NativeThemeSwitchAnimationModuleSpec, CAAnimationDelegate>
#else
#import <React/RCTBridgeModule.h>
@interface ThemeSwitchAnimationModule : RCTEventEmitter <RCTBridgeModule, CAAnimationDelegate>
#endif

@property (nonatomic, strong) UIImageView *overlayView;
@property (nonatomic, assign) BOOL isAnimating;
@property (nonatomic, strong) NSString *captureType;

- (void)captureAndDisplayScreen;
- (void)displayCapturedImageFullScreen:(UIImage *)image;
- (UIImage *)captureScreen;

@end



