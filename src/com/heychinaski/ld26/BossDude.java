package com.heychinaski.ld26;

import java.awt.Image;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;
import java.util.List;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;

public class BossDude extends BadDude {
  
  private static final int STATE_CHANGE_WAIT = 10000;
  int life = 200;
  private float FORWARD_SPEED = Ship.REGULAR_SPEED + 1;
  private List<RoundDude> roundDudes;
  private List<SquareDude> squareDudes;
  private List<TriDude> triangleDudes;
  private static final float UPDOWN_SPEED = 100;
  private static final float ROUND_DUDE_COUNT = 20;
  private static final float SQUARE_DUDE_COUNT = 30;
  private static final float TRIANGLE_DUDE_COUNT = 6;
  private static final int REFRESHING_ROUND_DUDES = 0;
  private static final int HOLDING_ROUND_DUDES = 1;
  private static final int REFRESHING_SQUARE_DUDES = 2;
  private static final int HOLDING_SQUARE_DUDES = 3;
  private static final int REFRESHING_TRIANGLE_DUDES = 4;
  private static final int HOLDING_TRIANGLE_DUDES = 5;
  
  private int state = REFRESHING_ROUND_DUDES;
  private long nextStateChange;
  
  private Game26 g26;
  
  public BossDude(Image image, Game26 game) {
    super(image);
    
    g26 = game;
    
    roundDudes = new ArrayList<RoundDude>();
    squareDudes = new ArrayList<SquareDude>();
    triangleDudes = new ArrayList<TriDude>();
    nextStateChange = System.currentTimeMillis() + 3000;
  }
  
  @Override
  public void collided(Entity e, float tick, Game game, Float boundsA,
      Float nextBoundsA, Float boundsB) {
    if(e instanceof PlayerMissile) {
      g26.removeMissile((PlayerMissile)e);
      g26.addExplosion(x, y);
      life -= 1;
      if(life == 0) {
        dead = true;
        g26.removeBadDude(this);
        for(int i = 0; i < 10; i++) {
          g26.addExplosion((float) ((x-w)+(Math.random() * w * 2)), 
                                      (float) ((y-h)+(Math.random() * h * 2)));
        }
        
        List<BadDude> allBadDudes = g26.badDudes;  
        while(!allBadDudes.isEmpty()) {
          BadDude bd = allBadDudes.get(0);
          g26.addExplosion(bd.x, bd.y);
          g26.removeBadDude(bd);
        }
      }
    }
  }
  
  @Override
  public void update(float tick, Game game) {
    
    if(g26.ourCamera.worldXToScreenX(x) > (game.getWidth() - 160)) {
      nextX += Math.max(-50 * tick, Math.min(50 * tick, (g26.player.x - 1000) - x));
    } else {
      nextX += (tick * FORWARD_SPEED);; 
    }
    nextY = ((float) Math.sin(((x) / UPDOWN_SPEED) * Math.PI)*100);
    
    if(System.currentTimeMillis() > nextStateChange) {
      nextState();
    }
    
    for(int i = 0; i < roundDudes.size(); i++) {
      RoundDude rd = roundDudes.get(i);
      if(rd.dead) roundDudes.remove(rd);
    }
    
    for(int i = 0; i < squareDudes.size(); i++) {
      SquareDude sd = squareDudes.get(i);
      if(sd.dead) squareDudes.remove(sd);
    }
    
    for(int i = 0; i < triangleDudes.size(); i++) {
      TriDude td = triangleDudes.get(i);
      if(td.dead) triangleDudes.remove(td);
    }
      
    if(roundDudes.isEmpty() && life > 10 && state == REFRESHING_ROUND_DUDES) {
      for(int i = 0; i < ROUND_DUDE_COUNT; i++) {
        RoundDude rd = g26.addRoundDude(800 + (i * 5), 50, 4, 60, this);
        roundDudes.add(rd);
        rd.angle = (float) ((Math.PI * 2) / ROUND_DUDE_COUNT) * i;
      }
      
      nextState();
    }
    
    if(squareDudes.size() < 3 && life > 10 && state == REFRESHING_SQUARE_DUDES) {
      for(int i = 0; i < SQUARE_DUDE_COUNT; i++) {
        SquareDude sd = g26.addSquareDude(300 + (i * (int)(Math.random() * 30)));
        squareDudes.add(sd);
      }
      
      nextState();
    }
    
    if(triangleDudes.isEmpty() && life > 10 && state == REFRESHING_TRIANGLE_DUDES) {
      TriDude previousDude = null;
      for(int i = 0; i < TRIANGLE_DUDE_COUNT; i++) {
        previousDude = g26.addTriDude(300 + (i * 50), -100, previousDude);
        triangleDudes.add(previousDude);
      }
      
      nextState();
    }
  }
  
  private void nextState() {
    nextStateChange = System.currentTimeMillis() + STATE_CHANGE_WAIT;
    state ++;
    if(state > HOLDING_TRIANGLE_DUDES) state = REFRESHING_ROUND_DUDES;
    System.out.println("State "+state);
  }

}
