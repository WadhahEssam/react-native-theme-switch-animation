package com.themeswitchanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

public class FadingImageView extends androidx.appcompat.widget.AppCompatImageView{
  private float gradientCenterX;
  private float gradientCenterY;
  private float gradientRadius;

  public FadingImageView(@NonNull Context context) {
    super(context);
  }

//  @Override
//  protected void onDraw(Canvas canvas) {
//    Drawable drawable = getDrawable();
//    if (drawable instanceof BitmapDrawable) {
//      Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
//      if (originalBitmap != null) {
//        // Create a copy of the original bitmap
//        Bitmap tempBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas tempCanvas = new Canvas(tempBitmap);
//
//        // Draw the gradient shader to apply the fading effect
//        RadialGradient radialGradient = new RadialGradient(
//          tempCanvas.getWidth() / 2,
//          tempCanvas.getHeight() / 2,
//          Math.max(tempCanvas.getWidth(), tempCanvas.getHeight()) / 2,
//          new int[]{0xFFFFFFFF, 0x00FFFFFF},
//          new float[]{0.3f, 0.6f},
//          Shader.TileMode.CLAMP);
//
//        Paint gradientPaint = new Paint();
//        gradientPaint.setShader(radialGradient);
//        gradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        tempCanvas.drawPaint(gradientPaint);
//
//        // Draw the modified bitmap on the canvas
//        canvas.drawBitmap(tempBitmap, 0, 0, null);
//      }
//    } else {
//      super.onDraw(canvas);
//    }
//  }

  public void animateFading(final float fromRadius, final float toRadius, long duration, final Runnable callback) {
    ValueAnimator animator = ValueAnimator.ofFloat(fromRadius, toRadius);
    animator.setDuration(duration);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        // Update the gradient radius
        gradientRadius = (float) valueAnimator.getAnimatedValue();
        // Invalidate the view to trigger a redraw
        invalidate();
      }


    });
    animator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        callback.run();
      }
    });
    animator.start();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Drawable drawable = getDrawable();
    if (drawable instanceof BitmapDrawable) {
      Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
      if (originalBitmap != null) {
        // Use properties for gradient center and radius
        gradientCenterX = getWidth() / 2;
        gradientCenterY = getHeight() / 2;

        // If gradientRadius has not been initialized, set it to the default
        if (gradientRadius == 0) {
          gradientRadius = Math.max(getWidth(), getHeight()) / 2;
        }


        Bitmap tempBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas tempCanvas = new Canvas(tempBitmap);

        // Use the animated gradient radius here
        RadialGradient radialGradient = new RadialGradient(
          gradientCenterX,
          gradientCenterY,
          gradientRadius,
          new int[]{0xFFFFFFFF, 0x00FFFFFF},
          new float[]{0.1f, 1f},
          Shader.TileMode.CLAMP);

        Paint gradientPaint = new Paint();
        gradientPaint.setShader(radialGradient);
        gradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        tempCanvas.drawPaint(gradientPaint);

        canvas.drawBitmap(tempBitmap, 0, 0, null);
      }
    } else {
      super.onDraw(canvas);
    }
  }
}
