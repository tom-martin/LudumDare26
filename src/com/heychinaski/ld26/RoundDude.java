package com.heychinaski.ld26;

import java.awt.Image;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class RoundDude extends BadDude {
  
  float angle = 0;
  private float originY;
  private float originX;
  private float spinSpeed;
  private float circleSize;
  
  private boolean circleStarted = false;
  private Entity originEntity;

  public RoundDude(Image image, float originX, float originY, float spinSpeed, float circleSize, Entity originEntity) {
    super(image);
    this.nextX = originX;
    this.nextY = originY;
    this.x = nextX;
    this.y = nextY;
    this.spinSpeed = spinSpeed;
    this.circleSize = circleSize;
    this.originEntity = originEntity;
    if(originEntity != null) {
      circleStarted = true;
    }
  }
  
  @Override
  public void update(float tick, Game game) {
    super.update(tick, game);
    
    Game26 g26 = (Game26) game;
    
    if(originEntity != null) {
      originX = originEntity.x;
      originY = originEntity.y;
    }
    
    if(!chargeMode) {
      if(x - g26.player.x < 300 || circleStarted) {
        if(!circleStarted) {
          
          if(originEntity == null) {
              originX = x;
              originY = y + (circleSize / 2);
          }
          angle = (float) (Math.PI);
          circleStarted = true;
        }
        angle += (tick * spinSpeed);
        nextX = (float) (originX + ((Math.sin(angle)) * circleSize));
        nextY = (float) (originY + (Math.cos(angle)) * circleSize);
        if(originEntity == null) originX += (tick * 90);
      } else {
        nextX += (tick * 25);
      }
    }
  }
}
