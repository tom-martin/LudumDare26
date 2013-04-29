package com.heychinaski.ld26;

import static java.lang.Math.random;
import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.heychinaski.engie.Entity;
import com.heychinaski.engie.Game;
import com.heychinaski.engie.camera.Camera;
import com.heychinaski.engie.camera.EntityTrackingCamera;
import com.heychinaski.engie.render.BackgroundTile;
import com.heychinaski.engie.render.Font;

public class Game26 extends Game {
  private static final long serialVersionUID = 1L;
  Ship player;
  private BackgroundTile sky1;
  private BackgroundTile sky2;
  private BackgroundTile back2;
  private BackgroundTile back3;
  List<BadDude> badDudes;
  EntityTrackingCamera ourCamera;
  private BufferedImage goodBuffer;
  private BufferedImage badBuffer;
  private Image heart;
  
  Font font;
  
  private Image ditherImage;
  
  public boolean alternateRender = false;

  public List<PlayerMissile> missiles;
  
  private float currentWipeX = 0;
  
  boolean pauseMotion = false;
  private Events events;
  Dialog dialog;
  private Clip musicClip;
  private BossDude bossDude = null;
  
  private Image powerUpImage;
  
  private Image titleImage;
  boolean drawTitle = false;
  
  @Override
  public void init() {
    init(0, 500);
  }

  public void init(int eventIndex, int fireFrequency) {
    bossDude = null;
    entities = new ArrayList<Entity>();
    player = new Ship(imageManager.get("ship2.png"),
                      imageManager.get("ship1.png"));
    
    player.fireFreq = fireFrequency;
    
    dialog = new Dialog();
    dialog.visible = false;
    dialog.text1 = "";
    dialog.text2 = "";
    dialog.text3 = "";
    dialog.image = imageManager.get("guy1.png");
    
    drawTitle = false;
    titleImage = imageManager.get("title.png");
    
    
    font = new Font(imageManager.get("font.png"), Color.black);
    dialog.font = font;
    
    heart = imageManager.get("heart.png");
    powerUpImage = imageManager.get("powerup.png");
    
    events = new Events(this, eventIndex);
    
    entities.add(player);
    missiles = new ArrayList<PlayerMissile>();
    badDudes = new ArrayList<BadDude>();
    
    sky1 = new BackgroundTile(imageManager.get("sky1.png"));
    sky2 = new BackgroundTile(imageManager.get("sky2.png"));
    back2 = new BackgroundTile(imageManager.get("back2.png"));
    back3 = new BackgroundTile(imageManager.get("back3.png"));
    
    ditherImage = imageManager.get("dither.png");
    
    ourCamera = new EntityTrackingCamera(this, player, true, false, 0, 0);
    ourCamera.zoom = 2.5f;
    
    entities.add(ourCamera);
    
    goodBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    badBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    
    currentWipeX = getWidth();
    
    dialog.x = (int) (5);
    dialog.y = (int) (256);
    
    if(musicClip == null) playMusic();
  }
  
  public void update(float tick) {
    events.update(this);
    if(player.life <= 0) {
      events.gameOver(this);
    }
    
    if(input.isKeyDown(KeyEvent.VK_ESCAPE)) {
      stop();
      System.exit(0);
    }
    
    if(input.isKeyDown(KeyEvent.VK_M)) {
      if(musicClip != null && musicClip.isRunning()) {
        musicClip.stop();
      } else {
        playMusic();
      }
      input.keyUp(KeyEvent.VK_M);
    }
    
    if(input.isKeyDown(KeyEvent.VK_P)) {
      pauseMotion = !pauseMotion;
      input.keyUp(KeyEvent.VK_P);
    }
    
    if(!pauseMotion) {
      ourCamera.toTrack = player;
      ourCamera.offsetX = player.currentOffset; 
      
      PlayerMissile removeThisTime = null;
      for(int i = 0; i < missiles.size(); i++) {
        PlayerMissile pm = missiles.get(i);
        if(ourCamera.worldXToScreenX(pm.x) > getWidth()) {
          removeThisTime = pm;
        }
      }
      if(removeThisTime != null) removeMissile(removeThisTime);
      
      float wipeX = player.x + 500;
      for(int i = 0; i < badDudes.size(); i++) {
        BadDude sd = badDudes.get(i);
        wipeX = Math.min(wipeX, sd.x - 32);
      }
      
      currentWipeX += Math.max(-500 * tick, Math.min(500 * tick, (wipeX - currentWipeX)));
      super.update(tick);
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
                         "guy2.png",
                         "heart.png",
                         "rounddude.png",
                         "powerup.png",
                         "powerupalt.png",
                         "tridude.png",
                         "bossdude.png",
                         "bossdialog.png",
                         "bossdialogdead.png",
                         "title.png"};
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
    
    badG.dispose();
    
    g.drawImage(goodBuffer, 0, 0, null);
    g.setClip(round(ourCamera.worldXToScreenX(currentWipeX)), 0, getWidth()* 100, getHeight());
    g.drawImage(badBuffer, 0, 0, null);
    g.setClip(null);
    g.scale(ourCamera.zoom, ourCamera.zoom);
    dialog.render(g);
    
    if(!drawTitle) {
      for(int i = 0; i < player.life; i++) {
        g.drawImage(heart, 5 + (i * heart.getWidth(null)), 5, null);
      }
      
      if(player.fireFreq <= 250) {
        g.drawImage(powerUpImage, 100, 5, null);
      }
      
      if(player.fireFreq <= 200) {
        g.drawImage(powerUpImage, 100 + (powerUpImage.getWidth(null)), 5, null);
      }
      
      if(player.fireFreq <= 150) {
        g.drawImage(powerUpImage, 100 + (powerUpImage.getWidth(null) * 2), 5, null);
      }
      
      if(bossDude != null) {
        g.setColor(Color.white);
        g.fillRect(200, 5, 200, 12);
        g.setColor(Color.black);
        g.fillRect(201, 6, 199, 10);
        g.setColor(Color.white);
        g.fillRect(202, 7, bossDude.life - 3, 8);
        g.setColor(Color.white);
        g.fillRect(203, 8, bossDude.life - 4, 6);
      }
    } else {
      g.drawImage(titleImage, 5, 5, null);
    }
  }
  
  public SquareDude addSquareDude() {
    return addSquareDude(500);
  }
  
  public SquareDude addSquareDude(int playerOffset) {
    SquareDude newSd = new SquareDude(imageManager.get("squaredude.png"));
    newSd.nextX = player.nextX + playerOffset;
    newSd.x = newSd.nextX;
    addBadDude(newSd);
    return newSd;
  }
  
  public PowerUp addPowerUp(int playerOffset, int newFreq) {
    PowerUp powerUp = new PowerUp(imageManager.get("powerup.png"), imageManager.get("powerupalt.png"), newFreq);
    powerUp.nextX = player.nextX + playerOffset;
    powerUp.x = powerUp.nextX;
    entities.add(powerUp);
    return powerUp;
  }
  
  public RoundDude addRoundDude(int playerOffset, float y, float spinSpeed, float circleSize, Entity originEntity) {
    RoundDude newDude = new RoundDude(imageManager.get("rounddude.png"), player.nextX + playerOffset, y, spinSpeed, circleSize, originEntity);
    addBadDude(newDude);
    return newDude;
  }
  
  public void addBadDude(BadDude sd) {
    entities.add(sd);
    badDudes.add(sd);
  }
  
  public TriDude addTriDude(float xOffset, float forwardSpeed, Entity toTrack) {
    Entity tt = toTrack == null ? player : toTrack;
    TriDude dude = new TriDude(imageManager.get("tridude.png"), tt, forwardSpeed);
    dude.nextX = player.x + xOffset;
    dude.x = dude.nextX;
    entities.add(dude);
    badDudes.add(dude);
    return dude;
  }
  
  public BossDude addBossDude() {
    BossDude dude = new BossDude(imageManager.get("bossdude.png"), this);
    dude.nextX = player.x + 300;
    dude.x = dude.nextX;
    entities.add(dude);
    badDudes.add(dude);
    
    bossDude  = dude;
    
    return dude;
  }
  
  public boolean removeBadDude(BadDude bd) {
    entities.remove(bd);
    bd.dead = true;
    return badDudes.remove(bd);
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
    playExplosionSound();
  }
  
  public void removeExplosion(Explosion e) {
    entities.remove(e);
  }

  public void removePowerUp(PowerUp p) {
    entities.remove(p);
  }
  
  public static int randomInt(int max) { return (int)(random() * max);}
  
  public synchronized void playShootSound() {
    playSound("/shoot" + System.currentTimeMillis() % 3 + ".wav", false);
  }
  
  public synchronized void playExplosionSound() {
    playSound("/explosion" + System.currentTimeMillis() % 3 + ".wav", false);
  }
  
  public synchronized void playPowerUpSound() {
    playSound("/powerup.wav", false);
  }
  
  public synchronized void playMusic() {
    musicClip = playSound("/music.wav", true);
    
    FloatControl gainControl = 
        (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
    gainControl.setValue(-5f);
  }
  
  public void stop() {
    try {
      if(musicClip != null) {
        musicClip.stop();
      }
    } catch(Exception e) {
      // meh
    }
  }
}
