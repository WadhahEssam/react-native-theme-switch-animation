package com.themeswitchanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
  private static final String TAG = "ThemeSwitchAnimationModule";
  private ReactContext reactContext;
  private ViewGroup rootView;
  private ImageView capturedImageView;
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
      freezeScreenLocal();
    }
  }

  @ReactMethod
  public void unfreezeScreen() {
    reactContext.runOnUiQueueThread(new Runnable() {
      @Override
      public void run() {
        if (isAnimating) {
          performFadeAnimation(capturedImageView);
        }
      }
    });
  }


  private void freezeScreenLocal() {
    this.isAnimating = true;
    this.rootView = (ViewGroup) getCurrentActivity().getWindow().getDecorView();
    this.capturedImageView = captureScreenshot(this.rootView);

    LinearLayout wrapper = new LinearLayout(this.reactContext);
    wrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    wrapper.addView(capturedImageView);

    reactContext.runOnUiQueueThread(new Runnable() {
      @Override
      public void run() {
        rootView.addView(wrapper);
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


  private ImageView captureScreenshot(View rootView) {
    Bitmap capturedImageBitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(capturedImageBitmap);
    rootView.draw(canvas);

    ImageView capturedImageView = new ImageView(this.reactContext);
    LinearLayout.LayoutParams fullScreenImageOverlayLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    capturedImageView.setLayoutParams(fullScreenImageOverlayLP);
    capturedImageView.setImageBitmap(capturedImageBitmap);
    capturedImageView.setVisibility(View.VISIBLE);

    return capturedImageView;
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
        capturedImageView.setVisibility(View.GONE);
        isAnimating = false;
      }
    });
    anim.start();
  }

  private void performFadeAnimation(final ImageView overlay) {
    // Create an ObjectAnimator that animates the 'alpha' property of overlay
    ObjectAnimator fadeOut = ObjectAnimator.ofFloat(overlay, "alpha", 1f, 0f);
    fadeOut.setDuration(500);

    // Set an AnimatorListener to hide the overlay and update the state when the animation ends
    fadeOut.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        overlay.setVisibility(View.GONE);
        isAnimating = false;
        // Update any other state variables, e.g., isAnimating = false;
      }
    });

    // Start the animation
    fadeOut.start();
  }

  @ReactMethod
  public void addListener(String eventName) {

  }

  @ReactMethod
  public void removeListeners(Integer count) {

  }

}
