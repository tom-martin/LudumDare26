package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class BadDude extends Entity {
  Image image;
  
  boolean chargeMode = false;
  protected boolean dead = false;

  private boolean reverseChargeMode;

  public BadDude(Image image) {
    super();
    this.image = image;
    this.w = image.getWidth(null);
    this.h = image.getHeight(null);
  }

  @Override
  public void collided(Entity e, float tick, Game game, Float boundsA,
      Float nextBoundsA, Float boundsB) {
    if(e instanceof PlayerMissile) {
      ((Game26)game).removeMissile((PlayerMissile)e);
      ((Game26)game).removeBadDude(this);
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
    
    if(x < (player.x - 300)) {
      chargeMode = true;
      reverseChargeMode = false;
    }
    
    if(chargeMode) {
      if(reverseChargeMode) {
        nextX += Math.max(-250 * tick, Math.min(250 * tick, (player.x - 350) - x));
        nextY += Math.max(-50 * tick, Math.min(50 * tick, (player.y - y)));
      } else {
        nextX += Math.max(-250 * tick, Math.min(250 * tick, (player.x + 350) - x));
        nextY += Math.max(-50 * tick, Math.min(50 * tick, (player.y - y)));
      }
      
      if(nextX > player.x + 300) {
        reverseChargeMode = true;
      }
    }
  }

}
