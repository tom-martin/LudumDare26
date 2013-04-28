package com.heychinaski.ld26;

import java.awt.Image;

import com.heychinaski.engie.Game;

public class SquareDude extends BadDude {
  private static final float FORWARD_SPEED = 25;
  private static final float UPDOWN_SPEED = 100;
  private int yDirection;
  private int yBounds;
  private int yCentre;
  
  public SquareDude(Image image) {
    super(image);
    yDirection = 1;
    if((int)(Math.random() * 2) == 0) {
      yDirection = -1;
    }
    
    yBounds = (int)(Math.random() * 100);
    yCentre = (int)(Math.random() * ((100 - yBounds) * 2)) - (100 - yBounds);
  }
  
  @Override
  public void update(float tick, Game game) {
    super.update(tick, game);
    
    if(!chargeMode) {
      nextX += (tick * FORWARD_SPEED);
      nextY = yCentre + ((float) Math.sin(((x) / UPDOWN_SPEED) * Math.PI)*yBounds*yDirection);
    }
  }

}
