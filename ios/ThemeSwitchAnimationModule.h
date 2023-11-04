
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface ThemeSwitchAnimationModule : RCTEventEmitter <RCTBridgeModule, CAAnimationDelegate>

@property (nonatomic, strong) UIImageView *overlayView;
@property (nonatomic, assign) BOOL isAnimating;

@end
