package com.themeswitchanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.react.bridge.ReactContext;

public class Animations {

  public static void performInvertedCircleAnimation(ImageView overlay, View rootView, long duration, double cxRatio, double cyRatio, Runnable callback) {
    int width = rootView.getWidth();
    int height = rootView.getHeight();

    int cx = (int) (width * cxRatio);
    int cy = (int) (height * cyRatio);

    float startRadius = Helpers.getPointMaxDistanceInsideContainer(cx, cy, width, height);

    Animator anim = ViewAnimationUtils.createCircularReveal(overlay, cx, cy, startRadius, 0);
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

  public static void performCircleAnimation(ImageView overlay, ViewGroup rootView, long duration, double cxRatio, double cyRatio, ReactContext reactContext, Runnable callback) {
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
            final Bitmap[] capturedImageBitmap = {ThemeSwitchAnimationModule.captureScreenshot(rootView, reactContext)};
            ImageView capturedImageAfterSwitching = ThemeSwitchAnimationModule.createImageView(capturedImageBitmap[0], reactContext);
            overlay.setVisibility(View.VISIBLE);

            rootView.addView(capturedImageAfterSwitching, new ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT));

            int width = rootView.getWidth();
            int height = rootView.getHeight();

            int cx = (int) (width * cxRatio);
            int cy = (int) (height * cyRatio);
            float finalRadius = Helpers.getPointMaxDistanceInsideContainer(cx, cy, width, height);

            Animator anim = ViewAnimationUtils.createCircularReveal(capturedImageAfterSwitching, cx, cy, 0, finalRadius);
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                capturedImageAfterSwitching.setVisibility(View.GONE);

                if (capturedImageBitmap[0] != null && !capturedImageBitmap[0].isRecycled()) {
                  capturedImageBitmap[0].recycle();
                  capturedImageBitmap[0] = null;
                }

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
