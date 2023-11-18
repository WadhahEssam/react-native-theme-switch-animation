package com.themeswitchanimation;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Promise;

abstract class ThemeSwitchAnimationModuleSpec extends ReactContextBaseJavaModule {
  ThemeSwitchAnimationModuleSpec(ReactApplicationContext context) {
    super(context);
  }
  
  public abstract void freezeScreen();
  public abstract void unfreezeScreen(String animationType, double duration, double cxRatio, double cyRatio);
}
