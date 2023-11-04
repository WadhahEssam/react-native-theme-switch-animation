//
//  Animation.h
//  Pods
//
//  Created by Wadah Esam on 04/11/2023.
//

#ifndef Animation_h
#define Animation_h

#import <UIKit/UIKit.h>
#import "ThemeSwitchAnimationModule.h"

@interface AnimationHelper : NSObject

+ (void)performFadeAnimation:(UIView *)overlayView
                    duration:(NSInteger)duration
                    callback:(void (^)(void))callback;

+ (void)performCircularAnimation:(UIView *)overlayView
                          source:(ThemeSwitchAnimationModule *)source
                        duration:(NSInteger)duration
                         cxRatio:(CGFloat)cxRatio
                         cyRatio:(CGFloat)cyRatio
                        callback:(void (^)(void))callback;

+ (void)performInvertedCircleAnimation:(UIView *)overlayView
                                source:(ThemeSwitchAnimationModule *)source
                              duration:(NSInteger)duration
                               cxRatio:(CGFloat)cxRatio
                               cyRatio:(CGFloat)cyRatio
                              callback:(void (^)(void))callback;

@end

#endif /* Animation_h */
