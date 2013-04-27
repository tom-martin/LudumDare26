package com.heychinaski.ld26;

import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D.Float;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class PlayerCar2 extends Entity {
  private double heading = 0;
  private double wheelBase = 80;
  private double speed = 0;
  private double slider = 0f;
  
  @Override
  public void collided(Entity arg0, float arg1, Game arg2, Float arg3,
      Float arg4, Float arg5) {
  }

  @Override
  public void render(Graphics2D g) {
    g.translate(x, y);
    g.rotate(heading);
    
    g.setColor(Color.blue);
    g.fillRect(round(-50), round(-40), 100, 80);
    g.rotate(-heading);
    g.translate(-x, -y);
    
    float frontWheelX = (float) (x + (wheelBase/2 * Math.cos(heading)));
    float frontWheelY = (float) (y + (wheelBase/2 * Math.sin(heading)));
    
    float sliderX = (float) (x + (slider/2 * Math.cos(heading+(Math.PI / 2))));
    float sliderY = (float) (y + (slider/2 * Math.sin(heading+(Math.PI / 2))));
    
    float backWheelX = (float) (x - (wheelBase/2 * Math.cos(heading)));
    float backWheelY = (float) (y - (wheelBase/2 * Math.sin(heading)));
    
    g.setColor(Color.red);
    g.fillRect(round(frontWheelX - 10), round(frontWheelY - 10), 10, 10);
    g.setColor(Color.yellow);
    g.fillRect(round(backWheelX - 10), round(backWheelY - 10), 10, 10);
    g.setColor(Color.green);
    g.fillRect(round(sliderX - 10), round(sliderY - 10), 10, 10);
  }

  @Override
  public void update(float tick, Game game) {
    if(game.input.isKeyDown(KeyEvent.VK_UP)) {
      speed = Math.min(3000, speed + (tick * 500));
    } else {
      speed = Math.max(0, speed - (tick * 500));
    }
    if(game.input.isKeyDown(KeyEvent.VK_DOWN)) speed -= (tick * 800);
    
    double steerAngle = 0;
    double steerDelta = 0.3; //((2000 - (speed * 0.75)) / 2000) * 0.50;
    if(game.input.isKeyDown(KeyEvent.VK_LEFT)) {
      steerAngle = -steerDelta;
      slider += Math.min(speed * 0.015, tick * (speed * 0.015));
    }
    if(game.input.isKeyDown(KeyEvent.VK_RIGHT)) {
      steerAngle = +steerDelta;
      slider -= Math.max(-speed * 0.015, tick * (speed * 0.015));
    }
    
    if(!game.input.isKeyDown(KeyEvent.VK_RIGHT) && !game.input.isKeyDown(KeyEvent.VK_LEFT)) {
      slider *= tick;
    }
    
    double frontWheelX = (x + (wheelBase/2 * Math.cos(heading)));
    double frontWheelY = (y + (wheelBase/2 * Math.sin(heading)));
    
    double backWheelX = (x - (wheelBase/2 * Math.cos(heading)));
    double backWheelY = (y - (wheelBase/2 * Math.sin(heading)));
    
    float sliderX = (float) (x + (slider/2 * Math.cos(heading+(Math.PI / 2))));
    float sliderY = (float) (y + (slider/2 * Math.sin(heading+(Math.PI / 2))));
    
    backWheelX += speed * tick * Math.cos(heading);
    backWheelY += speed * tick * Math.sin(heading);
    
    frontWheelX += speed * tick * Math.cos(heading+steerAngle);
    frontWheelY += speed * tick * Math.sin(heading+steerAngle);
    
    nextX = (float) ((frontWheelX + backWheelX + sliderX) / 3f);
    nextY = (float) ((frontWheelY + backWheelY + sliderY) / 3f);
    
    heading = Math.atan2(frontWheelY - backWheelY, frontWheelX - backWheelX);
  }

}
