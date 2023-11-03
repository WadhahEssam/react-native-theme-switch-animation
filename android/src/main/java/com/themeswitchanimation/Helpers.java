package com.themeswitchanimation;

public class Helpers {

  public static float getDistanceBetweenTwoPoints(float x1, float x2, float y1, float y2) {
    return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  }

  public static float getPointMaxDistanceInsideContainer(float x, float y, float containerWidth, float containerHeight) {
    float[][] corners = {{0, 0}, {containerWidth, 0}, {0, containerHeight}, {containerWidth, containerHeight}};
    float maxDistance = 0;

    for (float[] corner : corners) {
      float cornerX = corner[0];
      float cornerY = corner[1];
      float distanceBetweenTwoPoints = getDistanceBetweenTwoPoints(x, cornerX, y, cornerY);
      maxDistance = Math.max(maxDistance, distanceBetweenTwoPoints);
    }

    return maxDistance;
  }
}
