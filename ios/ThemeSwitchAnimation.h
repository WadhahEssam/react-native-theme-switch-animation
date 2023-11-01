
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNThemeSwitchAnimationSpec.h"

@interface ThemeSwitchAnimation : NSObject <NativeThemeSwitchAnimationSpec>
#else
#import <React/RCTBridgeModule.h>

@interface ThemeSwitchAnimation : NSObject <RCTBridgeModule>
#endif

@end
