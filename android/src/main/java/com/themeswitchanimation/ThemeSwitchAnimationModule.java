package com.themeswitchanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import javax.annotation.Nullable;

public class ThemeSwitchAnimationModule extends ReactContextBaseJavaModule {
  private static final String TAG = "ThemeSwitchAnimation";
  private ReactContext reactContext;
  private ViewGroup rootView;
  private ImageView fullScreenImageOverlay;
  private boolean isAnimating = false;

  public ThemeSwitchAnimationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return TAG;
  }

  @SuppressLint("LongLogTag")
  @ReactMethod
  public void freezeScreen() {
    Log.d(TAG, "freezeScreen: Freezing");
    System.out.println(isAnimating + " is Animating");
    if (!this.isAnimating) {
      System.out.println("switching");
      freezeScreenLocal(new Runnable() {
        @Override
        public void run() {
          System.out.println("running");
        }
      });
    }
  }

  @SuppressLint("LongLogTag")
  @ReactMethod
  public void unfreezeScreen() {
    Log.d(TAG, "freezeScreen: UNFreezing");
    System.out.println(isAnimating + " is Animating");

    reactContext.runOnUiQueueThread(new Runnable() {
      @Override
      public void run() {
        if (isAnimating) {
          performCircleAnimation(fullScreenImageOverlay);
        }
      }
    });
  }


  private void freezeScreenLocal(Runnable runnable) {
    this.isAnimating = true;
    this.rootView = (ViewGroup) getCurrentActivity().getWindow().getDecorView();

    LinearLayout wrapper = new LinearLayout(this.reactContext);
    wrapper.setOrientation(LinearLayout.VERTICAL);
    wrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

    ImageView fullScreenImageOverlay = new ImageView(this.reactContext);
    this.fullScreenImageOverlay = fullScreenImageOverlay;
    LinearLayout.LayoutParams fullScreenImageOverlayLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    fullScreenImageOverlay.setLayoutParams(fullScreenImageOverlayLP);
    Bitmap screenshot = captureScreenshot();
    fullScreenImageOverlay.setImageBitmap(screenshot);
    fullScreenImageOverlay.setVisibility(View.VISIBLE);

    wrapper.addView(fullScreenImageOverlay);

    reactContext.runOnUiQueueThread(new Runnable() {
      @Override
      public void run() {
        rootView.addView(wrapper);
        runnable.run();

        ReactApplicationContext context = getReactApplicationContext();
        sendEvent(context, "FINISHED_FREEZING_SCREEN", null);
      }
    });
  }


  private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }


  private Bitmap captureScreenshot() {
    rootView.setDrawingCacheEnabled(true);
    Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
    rootView.setDrawingCacheEnabled(false);
    int statusBarHeight = 0;

    // Get the navigation bar height
    int navigationBarHeight = 0;

    // Determine the height of the app content
    int contentHeight = bitmap.getHeight() - statusBarHeight - navigationBarHeight;

    // Crop the bitmap to include only the app content
    return Bitmap.createBitmap(bitmap, 0, statusBarHeight, bitmap.getWidth(), contentHeight);
  }

  private void performCircleAnimation(final ImageView overlay) {
    int cx = rootView.getWidth() / 2;
    int cy = rootView.getHeight() / 2;
    float finalRadius = Math.max(rootView.getWidth(), rootView.getHeight());

    Animator anim = ViewAnimationUtils.createCircularReveal(overlay, cx, cy, finalRadius, 0);
    anim.setDuration(1000);
    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        fullScreenImageOverlay.setVisibility(View.GONE);
        isAnimating = false;
      }
    });
    anim.start();
  }

}
