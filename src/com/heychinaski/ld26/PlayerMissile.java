package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class PlayerMissile extends Entity {

  private static final float FORWARD_SPEED = 800;
  private Image image1;
  private Image image2;
  
  public PlayerMissile(Image image1, Image image2) {
    this.image1 = image1;
    this.image2 = image2;
    this.w = image1.getWidth(null);
    this.h = image1.getHeight(null);
  }

  @Override
  public void update(float tick, Game game) {
    nextX += (tick * FORWARD_SPEED);
  }

  @Override
  public void render(Graphics2D g, Game game) {
    g.translate(x, y);
    Image i = ((Game26)game).alternateRender ? image2 : image1;
    g.drawImage(i, -8, -4, null);
    g.translate(-x, -y);
  }

  @Override
  public void collided(Entity with, float tick, Game game, Float bounds,
      Float nextBounds, Float withBounds) {
  }
}
