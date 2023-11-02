package com.themeswitchanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.react.bridge.ReactContext;

public class Animations {

  public static void performCircleAnimation(ImageView overlay, View rootView, long duration, Runnable callback) {
    int cx = rootView.getWidth() / 2;
    int cy = rootView.getHeight() / 2;
    float finalRadius = Math.max(rootView.getWidth(), rootView.getHeight());
    Animator anim = ViewAnimationUtils.createCircularReveal(overlay, cx, cy, finalRadius, 0);
    anim.setDuration(duration);
    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        overlay.setVisibility(View.GONE);
        callback.run();
      }
    });
    anim.start();
  }

  public static void performInvertedCircleAnimation(ImageView overlay, ViewGroup rootView, long duration, ReactContext reactContext, Runnable callback) {
    rootView.postOnAnimation(new Runnable() {
      private int frameCount = 0;

      @Override
      public void run() {
        frameCount++;
        if (frameCount < 2) {
          rootView.postOnAnimation(this);
        } else {
          reactContext.runOnUiQueueThread(() -> {
            // Creating another image after switching the theme
            // because we can't make the root view above the overlay
            overlay.setVisibility(View.GONE);
            ImageView capturedImageAfterSwitching = ThemeSwitchAnimationModule.captureScreenshot(rootView, reactContext);
            overlay.setVisibility(View.VISIBLE);

            rootView.addView(capturedImageAfterSwitching, new ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT));

            int width = rootView.getWidth();
            int height = rootView.getHeight();
            float diagonal = (float) Math.sqrt((width * width) + (height * height));
            int cx = width / 2;
            int cy = height / 2;

            Animator anim = ViewAnimationUtils.createCircularReveal(capturedImageAfterSwitching, cx, cy, 0, diagonal / 2);
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                capturedImageAfterSwitching.setVisibility(View.GONE);
                overlay.setVisibility(View.GONE);
                callback.run();
              }
            });

            anim.start();
          });
        }
      }
    });
  }

  public static void performFadeAnimation(final ImageView overlay, long duration, Runnable callback) {
    ObjectAnimator fadeOut = ObjectAnimator.ofFloat(overlay, "alpha", 1f, 0f);
    fadeOut.setDuration(duration);
    fadeOut.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        overlay.setVisibility(View.GONE);
        callback.run();
      }
    });
    fadeOut.start();
  }
}
