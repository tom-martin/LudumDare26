package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D.Float;


import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class Ship extends Entity {
  public static float SLOW_SPEED = 50;
  public static float REGULAR_SPEED = 100;
  public static float FAST_SPEED = 150;
  public static float UPDOWN_SPEED = 300;
  
  public long lastFire = -1;
  
  public Image image1, image2;

  public Ship(Image image1, Image image2) {
    super();
    this.image1 = image1;
    this.image2 = image2;
    lastFire = System.currentTimeMillis();
    this.w = image1.getWidth(null);
    this.h = image1.getHeight(null);
  }

  @Override
  public void collided(Entity other, float tick, Game game, Float arg3,
      Float arg4, Float arg5) {
    if(other instanceof SquareDude) {
      ((Game26)game).removeSquareDude((SquareDude) other);
      ((Game26)game).addExplosion(other.x, other.y);
    }
  }

  @Override
  public void render(Graphics2D g, Game game) {
    g.translate(x, y);
    Image i = ((Game26)game).alternateRender ? image2 : image1;
    g.drawImage(i, -16, 8, null);
    g.translate(-x, -y);
  }

  @Override
  public void update(float tick, Game game) {
    float speed = REGULAR_SPEED;
    if(game.input.isKeyDown(KeyEvent.VK_LEFT)) speed = SLOW_SPEED;
    if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) speed = FAST_SPEED;
    nextX += (tick * speed);
    if(game.input.isKeyDown(KeyEvent.VK_UP)) nextY -= (tick * UPDOWN_SPEED);
    if(game.input.isKeyDown(KeyEvent.VK_DOWN)) nextY += (tick * UPDOWN_SPEED);
    
    nextY = Math.min(nextY, 100);
    nextY = Math.max(nextY, -144);
    
    if(game.input.isKeyDown(KeyEvent.VK_SPACE) &&
        System.currentTimeMillis() - lastFire > 500) {
      ((Game26)game).addMissile(nextX, nextY + 20);
      lastFire = System.currentTimeMillis();
    }
  }

}
