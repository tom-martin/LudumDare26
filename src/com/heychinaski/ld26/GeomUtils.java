package com.heychinaski.ld26;

import java.awt.geom.Point2D;

public class GeomUtils {
  public static void rotate(Point2D.Float point, float rotation) {
    float cs = (float) Math.cos(rotation);
    float sn = (float) Math.sin(rotation);
    
    float newX = (point.x * cs) - (point.y * sn);
    float newY = (point.x * sn) + (point.y * cs);
    point.x = newX;
    point.y = newY;
  }
}
