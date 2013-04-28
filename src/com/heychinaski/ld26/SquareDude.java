package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class SquareDude extends Entity {
  private static final float FORWARD_SPEED = 25;
  private static final float UPDOWN_SPEED = 50;
  
  Image image;
  
  int yDirection = -1;
  
  long lastDirectionChange;
  long nextDirectionChange = Math.round(Math.random() * 5000);
  
  boolean chargeMode = false;
  protected boolean dead = false;

  public SquareDude(Image image) {
    super();
    this.image = image;
    this.w = image.getWidth(null);
    this.h = image.getHeight(null);
    lastDirectionChange = System.currentTimeMillis();
  }

  @Override
  public void collided(Entity e, float tick, Game game, Float boundsA,
      Float nextBoundsA, Float boundsB) {
    if(e instanceof PlayerMissile) {
      ((Game26)game).removeMissile((PlayerMissile)e);
      ((Game26)game).removeSquareDude(this);
      ((Game26)game).addExplosion(x, y);
      dead = true;
    }
  }

  @Override
  public void render(Graphics2D g, Game game) {
    g.translate(x, y);
    Image i = image;
    g.drawImage(i, -8, -8, null);
    g.translate(-x, -y);
  }

  @Override
  public void update(float tick, Game game) {
    Ship player = ((Game26)game).player;
    
    if(x < (player.x - 50)) {
      chargeMode = true;
    }
    
    if(chargeMode) {
      nextX += Math.max(-250 * tick, Math.min(250 * tick, (player.x - x)));
      nextY += Math.max(-250 * tick, Math.min(250 * tick, (player.y - y)));
      return;
    }
      
    if(y < -100) {
      yDirection = -1;
      nextY = -100;
      y = -100;
    }
    
    if(y > 100) {
      yDirection = 1;
      nextY = 100;
      y = 100;
    }
    
    if(System.currentTimeMillis() > (lastDirectionChange + nextDirectionChange)) {
      yDirection *= -1;
      lastDirectionChange = System.currentTimeMillis();
      nextDirectionChange = Math.round(Math.random() * 5000);
    }
    
    nextX += (tick * FORWARD_SPEED);
    nextY -= (tick * UPDOWN_SPEED * yDirection);
  }

}
