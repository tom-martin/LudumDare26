package com.heychinaski.ld26;

import java.awt.Graphics2D;
import java.awt.Image;

import com.heychinaski.engie.render.Font;

public class Dialog {
  public String text1 = "";
  public String text2 = "";
  public String text3 = "";
  public Image image;
  public Font font;
  public int x = 0;
  public int y = 0;
  public boolean visible = false;
  
  public void render(Graphics2D g) {
    if(!visible) return;
    g.drawImage(image, x, y, null);
    font.renderString(g, text1, x + image.getWidth(null) + 5, y);
    font.renderString(g, text2, x + image.getWidth(null) + 5, y+10);
    font.renderString(g, text3, x + image.getWidth(null) + 5, y+20);
  }
}
