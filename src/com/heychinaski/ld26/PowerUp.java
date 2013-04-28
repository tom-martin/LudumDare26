package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class PowerUp extends Entity {
  
  private Image image;
  
  boolean collected = false;
  
  public PowerUp(Image image, Image altImage) {
    super();
    this.image = image;
    this.altImage = altImage;
    this.w = image.getWidth(null);
    this.h = image.getHeight(null);
  }

  private Image altImage;

  @Override
  public void update(float tick, Game game) {
    nextX += (tick * 25);
    nextY = ((float) Math.sin(((x) / 100) * Math.PI)*100);
    
    
    Ship player = ((Game26)game).player;
    
    if(nextX < player.x - 50) {
      nextX = player.x + 1200;
    }
  }

  @Override
  public void render(Graphics2D g, Game game) {
    g.translate(x, y);
    Image i = ((Game26)game).alternateRender ? altImage : image;
    g.drawImage(i, -8, -8, null);
    g.translate(-x, -y);
  }

  @Override
  public void collided(Entity with, float tick, Game game, Float bounds,
      Float nextBounds, Float withBounds) {
    
  }
  
}
