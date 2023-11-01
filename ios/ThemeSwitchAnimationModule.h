
//#ifdef RCT_NEW_ARCH_ENABLED
//#import "RNThemeSwitchAnimationSpec.h"
//
//@interface ThemeSwitchAnimationModule : NSObject <NativeThemeSwitchAnimationSpec>
//#else
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface ThemeSwitchAnimationModule : RCTEventEmitter <RCTBridgeModule>
//#endif

@end
