package com.heychinaski.ld26;

import java.awt.Image;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class TriDude extends BadDude {
  
  private float forwardSpeed = -100;
  private static final float UPDOWN_SPEED = 25;
  private Entity yTarget;

  public TriDude(Image image, Entity yTarget, float forwardSpeed) {
    super(image);
    this.yTarget = yTarget;
    this.forwardSpeed = forwardSpeed;
  }
  
  @Override
  public void update(float tick, Game game) {
    super.update(tick, game);
    
    if(!chargeMode) {
      nextY += Math.max(-UPDOWN_SPEED * tick, Math.min(UPDOWN_SPEED * tick, (yTarget.y - y)));
      nextX += (tick * forwardSpeed);
    }
  }
}
