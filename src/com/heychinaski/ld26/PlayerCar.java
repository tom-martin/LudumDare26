package com.heychinaski.ld26;

import static java.lang.Math.round;
import static java.lang.Math.min;
import static java.lang.Math.max;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Float;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class PlayerCar extends Entity {
  
  private float xMomentum = 0;
  private float yMomentum = 0;
  private float rotation = 0;
  private float accel = 0;
  
  private Point2D.Float accelBuffer = new Point2D.Float();
  
  private static float SLIDINESS = 0.1f;

  @Override
  public void collided(Entity arg0, float arg1, Game arg2, Float arg3,
      Float arg4, Float arg5) {
    
  }

  @Override
  public void render(Graphics2D g) {
    g.translate(x, y);
    g.rotate(rotation);
    
    g.setColor(Color.blue);
    g.fillRect(round(- 40), round(-80), 80, 100);
    g.rotate(-rotation);
    g.translate(-x, -y);
  }

  @Override
  public void update(float tick, Game game) {
    if(game.input.isKeyDown(KeyEvent.VK_LEFT)) rotation  -= 3 * tick;
    if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) rotation += 3 * tick;
    
    accel = max(0, (accel - tick / 2));
    
    if(game.input.isKeyDown(KeyEvent.VK_UP)) accel += (3 * tick);
    if(game.input.isKeyDown(KeyEvent.VK_DOWN)) accel -= (20 * tick);
    
    if(!game.input.isKeyDown(KeyEvent.VK_UP) && !game.input.isKeyDown(KeyEvent.VK_DOWN)) accel -= (2 * tick);
    
    accelBuffer.y = max(-5f, min(20f, accel));
    accelBuffer.x = 0;
    GeomUtils.rotate(accelBuffer, rotation);
    
    xMomentum = (((xMomentum * SLIDINESS) + accelBuffer.x) / (SLIDINESS + 1));
    yMomentum = (((yMomentum * SLIDINESS) + accelBuffer.y) / (SLIDINESS + 1));
    
    nextX = x + (xMomentum * (tick * 200));
    nextY = y + (yMomentum * (tick * 200));
  }

}
