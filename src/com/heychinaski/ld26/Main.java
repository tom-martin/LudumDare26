package com.heychinaski.ld26;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.heychinaski.engie.Game;
import com.heychinaski.engie.camera.Camera;
import com.heychinaski.engie.camera.EntityTrackingCamera;

public class Main { 

/**
 * @param args
 */
public static void main(String[] args) {
  JFrame mainWindow = new JFrame("Test game");
  JPanel panel = (JPanel) mainWindow.getContentPane();
  
  panel.setPreferredSize(new Dimension(1024, 768));
  panel.setLayout(new BorderLayout());
  
  final Game game = new Game() {
    
    private PlayerCar2 playerCar;

    @Override
    public void init() {
      playerCar = new PlayerCar2();
      entities.add(playerCar);
    }
    
    public void update(float tick) {
      if(input.isKeyDown(KeyEvent.VK_ESCAPE)) System.exit(0);
      
      if(input.isKeyDown(KeyEvent.VK_Z)) camera.zoom += tick;
      if(input.isKeyDown(KeyEvent.VK_X)) camera.zoom -= tick;
      super.update(tick);
    }

    @Override
    public String[] images() {
      return new String[] {"test_track.png"};
    }

    @Override
    public Camera camera() {
      Camera camera = new EntityTrackingCamera(playerCar, this);
      camera.zoom = 1;
      return camera;
    }

    @Override
    public Image bgTileImage() {
      Image image = new BufferedImage(500, 500, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics g = image.getGraphics();
      g.setColor(Color.pink);
      g.fillRect(0, 0, 500, 500);
      
      g.setColor(Color.red);
      g.fillRect(0, 0, 250, 250);
      g.fillRect(250, 250, 250, 250);
      g.dispose();
      return image;
//      Image i = imageManager.get("test_track.png");
//      BufferedImage bigger = new BufferedImage(5000, 5000, BufferedImage.TYPE_INT_ARGB);
//      bigger.getGraphics().drawImage(i, 0, 0, 5000, 5000, null);
//      return bigger;
    }
    
  };
  panel.add(game);
  
  mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  mainWindow.pack();
  
  mainWindow.setCursor(mainWindow.getToolkit().createCustomCursor(
      new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
      "null"));
  
  new Thread() {
    public void run() {
      game.start();
    }
  }.start();
  
  game.requestFocus();
  mainWindow.setVisible(true);
}

}