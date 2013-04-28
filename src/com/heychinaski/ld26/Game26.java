package com.heychinaski.ld26;

import static java.lang.Math.floor;
import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;
import com.heychinaski.engie.camera.Camera;
import com.heychinaski.engie.camera.EntityTrackingCamera;
import com.heychinaski.engie.render.BackgroundTile;
import com.heychinaski.engie.render.Font;

public class Game26 extends Game {
  Ship player;
  private BackgroundTile sky1;
  private BackgroundTile sky2;
  private BackgroundTile back2;
  private BackgroundTile back3;
  private List<SquareDude> squareDudes;
  private EntityTrackingCamera ourCamera;
  private BufferedImage goodBuffer;
  private BufferedImage badBuffer;
  
  Font font;
  
  private Image ditherImage;
  
  public boolean alternateRender = false;

  public List<PlayerMissile> missiles;
  
  private float currentWipeX = 0;
  
  boolean pauseMotion = false;
  private Events events;
  Dialog dialog;

  @Override
  public void init() {
    player = new Ship(imageManager.get("ship2.png"),
                      imageManager.get("ship1.png"));
    
    dialog = new Dialog();
    dialog.visible = false;
    dialog.text1 = "";
    dialog.text2 = "";
    dialog.text3 = "";
    dialog.image = imageManager.get("guy1.png");
    
    
    font = new Font(imageManager.get("font.png"), Color.black);
    dialog.font = font;
    
    events = new Events(this);
    
    entities.add(player);
    missiles = new ArrayList<PlayerMissile>();
    squareDudes = new ArrayList<SquareDude>();
    
    sky1 = new BackgroundTile(imageManager.get("sky1.png"));
    sky2 = new BackgroundTile(imageManager.get("sky2.png"));
    back2 = new BackgroundTile(imageManager.get("back2.png"));
    back3 = new BackgroundTile(imageManager.get("back3.png"));
    
    ditherImage = imageManager.get("dither.png");
    
    ourCamera = new EntityTrackingCamera(this, player, true, false, 160, 0);
    ourCamera.zoom = 2.5f;
    
    entities.add(ourCamera);
    
    goodBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    badBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    
    currentWipeX = getWidth();
  }
  
  public void update(float tick) {
    events.update(this);
    if(input.isKeyDown(KeyEvent.VK_ESCAPE)) System.exit(0);
    
    if(input.isKeyDown(KeyEvent.VK_Z)) ourCamera.zoom += tick;
    if(input.isKeyDown(KeyEvent.VK_X)) ourCamera.zoom -= tick;
    
    if(input.isKeyDown(KeyEvent.VK_P)) {
      pauseMotion = !pauseMotion;
      input.keyUp(KeyEvent.VK_P);
    }
    
    if(!pauseMotion) {
      Entity tracking = player;
      for(int i = 0; i < squareDudes.size(); i++) {
        SquareDude sd = squareDudes.get(i);
        if(sd.x < player.x) {
          tracking = sd;
        }
      }
      
      ourCamera.toTrack = tracking;
      
//      if(squareDudes.size() < 5 &&
//          System.currentTimeMillis() - lastSquareDudeAddition > nextSquareDudeAddition) {
//        SquareDude newSd = new SquareDude(imageManager.get("squaredude.png"));
//        newSd.nextX = player.nextX + 500;
//        newSd.x = newSd.nextX;
//        addSquareDude(newSd);
//      }
      
      PlayerMissile removeThisTime = null;
      for(int i = 0; i < missiles.size(); i++) {
        PlayerMissile pm = missiles.get(i);
        if(ourCamera.worldXToScreenX(pm.x) > getWidth()) {
          removeThisTime = pm;
        }
      }
      if(removeThisTime != null) removeMissile(removeThisTime);
      
      float wipeX = player.x + 500;
      for(int i = 0; i < squareDudes.size(); i++) {
        SquareDude sd = squareDudes.get(i);
        wipeX = Math.min(wipeX, sd.x);
      }
      
      currentWipeX += Math.max(-500 * tick, Math.min(500 * tick, (wipeX - currentWipeX)));
      super.update(tick);
      dialog.x = (int) ((ourCamera.x) - 180);
      dialog.y = (int) ((ourCamera.y) + 118);
    }
  }

  @Override
  public String[] images() {
    return new String[] {"ship1.png", 
                         "ship2.png",
                         "sky1.png",
                         "sky2.png",
                         "back2.png",
                         "back3.png",
                         "squaredude.png",
                         "missile1.png",
                         "missile2.png",
                         "dither.png",
                         "explosion1.png",
                         "explosion2.png",
                         "explosion3.png",
                         "explosion1alt.png",
                         "explosion2alt.png",
                         "explosion3alt.png",
                         "font.png",
                         "guy1.png",
                         "guy2.png"};
  }

  @Override
  public Camera camera() {
    return null;
  }

  @Override
  public Image bgTileImage() {
    return null;
  }
  
  @Override
  public void render(Graphics2D g) {
    Graphics2D goodG = (Graphics2D) goodBuffer.getGraphics();
    alternateRender = false;
    ourCamera.look(goodG);
    
    sky1.render(round(ourCamera.x-((getWidth() / 2) * (1 / ourCamera.zoom))), 
        -160,
        round(ourCamera.x+((getWidth() / 2) * (1 / ourCamera.zoom))), goodG);
    back2.render(round(ourCamera.x-((getWidth() / 2) * (1 / ourCamera.zoom))), 
                 128,
                 round(ourCamera.x+((getWidth() / 2) * (1 / ourCamera.zoom))), goodG);
    
    goodG.drawImage(ditherImage, round(currentWipeX-30), -160, null);
    for(int i = 0; i < entities.size(); i++) {
      entities.get(i).render(goodG, this);
    }
    
    dialog.render(goodG);
    goodG.dispose();
    
    Graphics2D badG = (Graphics2D) badBuffer.getGraphics();
    alternateRender = true;
    badG.setColor(Color.white);
    badG.fillRect(0, 0, getWidth(), getHeight());
    ourCamera.look(badG);
    
    sky2.render(round(ourCamera.x-((getWidth() / 2) * (1 / ourCamera.zoom))), 
        -160,
        round(ourCamera.x+((getWidth() / 2) * (1 / ourCamera.zoom))), badG);
    back3.render(round(ourCamera.x-((getWidth() / 2) * (1 / ourCamera.zoom))), 
                 128,
                 round(ourCamera.x+((getWidth() / 2) * (1 / ourCamera.zoom))), badG);
    for(int i = 0; i < entities.size(); i++) {
      entities.get(i).render(badG, this);
    }
    
    dialog.render(badG);
    badG.dispose();
    
    g.drawImage(goodBuffer, 0, 0, null);
    g.setClip(round(ourCamera.worldXToScreenX(currentWipeX)), 0, getWidth(), getHeight());
    g.drawImage(badBuffer, 0, 0, null);
  }
  
  public SquareDude addSquareDude() {
    return addSquareDude(500);
  }
  
  public SquareDude addSquareDude(int playerOffset) {
    SquareDude newSd = new SquareDude(imageManager.get("squaredude.png"));
    newSd.nextX = player.nextX + playerOffset;
    newSd.x = newSd.nextX;
    addSquareDude(newSd);
    return newSd;
  }
  
  public void addSquareDude(SquareDude sd) {
    entities.add(sd);
    squareDudes.add(sd);
  }
  
  public boolean removeSquareDude(SquareDude sd) {
    entities.remove(sd);
    sd.dead = true;
    return squareDudes.remove(sd);
  }
  
  public boolean addMissile(float x, float y) {
    PlayerMissile missile = new PlayerMissile(imageManager.get("missile1.png"), imageManager.get("missile2.png"));
    missile.nextX = x;
    missile.nextY = y;
    entities.add(missile);
    return missiles.add(missile);
  }
  
  public boolean removeMissile(PlayerMissile m) {
    entities.remove(m);
    return missiles.remove(m);
  }
  
  public void addExplosion(float x, float y) {
    Explosion e = new Explosion(imageManager.get("explosion1.png"),
                                imageManager.get("explosion2.png"),
                                imageManager.get("explosion3.png"),
                                imageManager.get("explosion1alt.png"),
                                imageManager.get("explosion2alt.png"),
                                imageManager.get("explosion3alt.png"));
    e.nextX = x;
    e.nextY = y;
    entities.add(e);
  }
  
  public void removeExplosion(Explosion e) {
    entities.remove(e);
  }

}
