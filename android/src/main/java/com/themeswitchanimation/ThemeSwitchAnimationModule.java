package com.themeswitchanimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class ThemeSwitchAnimationModule extends ReactContextBaseJavaModule {
  private static final String TAG = "ThemeSwitchAnimationModule";
  private ReactContext reactContext;
  private ViewGroup rootView;
  private ImageView capturedImageView;
  private Bitmap capturedImageBitmap;
  private boolean isAnimating = false;

  public ThemeSwitchAnimationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return TAG;
  }

  @ReactMethod
  public void freezeScreen() {
    if (!this.isAnimating) {
      this.isAnimating = true;
      this.rootView = (ViewGroup) getCurrentActivity().getWindow().getDecorView();
      this.capturedImageBitmap = captureScreenshot(this.rootView, this.reactContext);
      this.capturedImageView = createImageView(this.capturedImageBitmap, this.reactContext);

      reactContext.runOnUiQueueThread(() -> {
        rootView.addView(capturedImageView, new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT));
        reactContext
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
          .emit("FINISHED_FREEZING_SCREEN", null);
      });
    }
  }

  @ReactMethod
  public void unfreezeScreen(String animationType, int duration, float cxRatio, float cyRatio) {
    reactContext.runOnUiQueueThread(new Runnable() {
      @Override
      public void run() {
        if (isAnimating) {
          switch (animationType) {
            case "circular":
              Animations.performCircleAnimation(capturedImageView, rootView, duration, cxRatio, cyRatio, reactContext, new Runnable() {
                @Override
                public void run() {
                  cleanUp();
                }
              });
              break;
            case "circular-inverted":
              Animations.performInvertedCircleAnimation(capturedImageView, rootView, duration, cxRatio, cyRatio, new Runnable() {
                @Override
                public void run() {
                  cleanUp();
                }
              });
              break;
            case "fade":
            default:
              Animations.performFadeAnimation(capturedImageView, duration, new Runnable() {
                @Override
                public void run() {
                  cleanUp();
                }
              });
              break;
          }
        }
      }
    });
  }

  public void cleanUp() {
    if (capturedImageBitmap != null && !capturedImageBitmap.isRecycled()) {
      capturedImageBitmap.recycle();
      capturedImageBitmap = null;
    }
    rootView.removeView(capturedImageView);
    isAnimating = false;
  }

  public static Bitmap captureScreenshot(View rootView, ReactContext reactContext) {
    Bitmap capturedImageBitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(capturedImageBitmap);
    rootView.draw(canvas);

    return capturedImageBitmap;
  }

  public static ImageView createImageView(Bitmap capturedImageBitmap, ReactContext reactContext) {
    ImageView capturedImageView = new ImageView(reactContext);
    LinearLayout.LayoutParams fullScreenImageOverlayLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    capturedImageView.setLayoutParams(fullScreenImageOverlayLP);
    capturedImageView.setImageBitmap(capturedImageBitmap);
    capturedImageView.setVisibility(View.VISIBLE);

    return capturedImageView;
  }

  @ReactMethod
  public void addListener(String eventName) {}

  @ReactMethod
  public void removeListeners(Integer count) {}
}
