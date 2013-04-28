package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D.Float;


import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class Ship extends Entity {
  public static float SLOW_SPEED = 0;
  public static float REGULAR_SPEED = 100;
  public static float FAST_SPEED = 200;
  public static float UPDOWN_SPEED = 300;
  public static int DEATH_COOL_OFF_TIME = 2000;
  
  public int life = 5;
  
  public boolean deathCoolOff = false;
  
  public long lastFire = -1;
  
  public Image image1, image2;
  
  public int fireFreq = 500;
  
  public float currentOffset = 160;
  public float targetOffset = 160;
  private long deathCoolOffCompleteTime;

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
    if(other instanceof BadDude && !deathCoolOff) {
      if(!(other instanceof BossDude)) ((Game26)game).removeBadDude((BadDude) other);
      ((Game26)game).addExplosion(other.x, other.y);
      life--;
      deathCoolOff = true;
      deathCoolOffCompleteTime = System.currentTimeMillis() + DEATH_COOL_OFF_TIME;
    }
    if(other instanceof PowerUp) {
      PowerUp p = (PowerUp) other;
      if(!p.collected) {
        ((Game26)game).removePowerUp(p);
        p.collected = true;
        ((Game26)game).playPowerUpSound();
        if(p.newFreq > 0) fireFreq = p.newFreq;
      }
    }
  }

  @Override
  public void render(Graphics2D g, Game game) {
    if(deathCoolOff && (System.currentTimeMillis() / 200) % 2 == 0) return;
    g.translate(x, y);
    Image i = ((Game26)game).alternateRender ? image2 : image1;
    g.drawImage(i, -16, -8, null);
    g.translate(-x, -y);
  }

  @Override
  public void update(float tick, Game game) {
    if(System.currentTimeMillis() > deathCoolOffCompleteTime) {
      deathCoolOff = false;
    }
    
    float speed = REGULAR_SPEED;
    if(game.input.isKeyDown(KeyEvent.VK_LEFT)) {
      if(Math.abs(currentOffset - targetOffset) > 1) {
        speed = SLOW_SPEED;
      } else {
        speed = REGULAR_SPEED;
      }
      targetOffset = 170;
    }
    if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) {
      if(Math.abs(currentOffset - targetOffset) > 1) {
        speed = FAST_SPEED;
      } else {
        speed = REGULAR_SPEED;
      }
      targetOffset = 0;
    }
    nextX += (tick * speed);
    if(game.input.isKeyDown(KeyEvent.VK_UP)) nextY -= (tick * UPDOWN_SPEED);
    if(game.input.isKeyDown(KeyEvent.VK_DOWN)) nextY += (tick * UPDOWN_SPEED);
    
    nextY = Math.min(nextY, 100);
    nextY = Math.max(nextY, -144);
    
    if(game.input.isKeyDown(KeyEvent.VK_SPACE) &&
        !((Game26)game).dialog.visible &&
        System.currentTimeMillis() - lastFire > fireFreq) {
      ((Game26)game).addMissile(nextX, nextY + 8);
      ((Game26)game).playShootSound();
      lastFire = System.currentTimeMillis();
    }
    
    float offsetSpeed = Math.abs(REGULAR_SPEED - speed);
    currentOffset += Math.max(-offsetSpeed * tick, Math.min(offsetSpeed * tick, (targetOffset - currentOffset)));
  }

}
