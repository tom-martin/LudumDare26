package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class Explosion extends Entity {
  
  Image image1;
  private long created;
  private Image image2;
  private Image image3;
  private Image altImage1;
  private Image altImage2;
  private Image altImage3;

  public Explosion(Image image1,
                    Image image2,
                    Image image3,
                    Image altImage1,
                    Image altImage2,
                    Image altImage3) {
    super();
    this.image1 = image1;
    this.image2 = image2;
    this.image3 = image3;
    this.altImage1 = altImage1;
    this.altImage2 = altImage2;
    this.altImage3 = altImage3;
    created = System.currentTimeMillis();
  }

  @Override
  public void update(float tick, Game game) {
    if(System.currentTimeMillis() - created > 500) {
      ((Game26)game).removeExplosion(this);
    }
  }

  @Override
  public void render(Graphics2D g, Game game) {
    g.translate(x, y);
    
    boolean alt = ((Game26)game).alternateRender;
    Image i = alt ? altImage1 : image1;
    if(System.currentTimeMillis() - created > 200) {
      i = alt ? altImage2 : image2;
    }
    if(System.currentTimeMillis() - created > 400) {
      i = alt ? altImage3 : image3;
    }
    g.drawImage(i, -8, -8, null);
    g.translate(-x, -y);
  }

  @Override
  public void collided(Entity with, float tick, Game game, Float bounds,
      Float nextBounds, Float withBounds) {
    
  }

}
