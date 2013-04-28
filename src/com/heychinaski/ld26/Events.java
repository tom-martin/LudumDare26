package com.heychinaski.ld26;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Events {
  public List<Event> events = new ArrayList<Event>();
  
  public Event currentEvent;
  public int currentEventIndex = 0;
  
  public long nextEventTime = 0;
  public boolean currentEventStarted = false;
  
  public Events(Game26 game) {
    events.add(new DialogEvent(2000, "TAKE IT EASY OUT THERE PILOT.", "", "<PUSH SPACE>", game.imageManager.get("guy1.png")));
    events.add(new DialogEvent(0, "WILL DO COMMAND. SHOULD BE A", "ROUTINE MISSION.", "", game.imageManager.get("guy2.png")));
    events.add(new Event(5000) {
      private SquareDude dude;

      @Override
      public void start(Game26 game) {
        dude = game.addSquareDude();
      }

      @Override
      public boolean isComplete(Game26 game) {
        return dude != null 
            && (dude.x - game.player.x < 300);
      }
      
      @Override
      public void end(Game26 game) {
      }
      
      @Override
      public DialogEvent chainedEvent(Game26 game) {
        return new DialogEvent(0, "HOLY POTATOES! WHAT IS THAT?", "", "", game.imageManager.get("guy2.png")) {
          @Override
          public Event chainedEvent(Game26 game) {
            return new DialogEvent(0, "UNKNOWN. AND IT APPEARS TO BE", "AFFECTING THE SPACE TIME AROUND IT", "", game.imageManager.get("guy1.png")) {
              @Override
              public Event chainedEvent(Game26 game) {
                return new DialogEvent(0, "APPEARS HOSTILE. PERMISSION TO", "ENGAGE?", "", game.imageManager.get("guy2.png")) {
                  @Override
                  public Event chainedEvent(Game26 game) {
                    return new DialogEvent(0, "IDENTIFIED HOSTILE AS A", "MINIMALISOR. THEY MINIMALISE", "EVERYTHING AROUND THEM.", game.imageManager.get("guy1.png")) {
                      @Override
                      public Event chainedEvent(Game26 game) {
                        return new DialogEvent(0, "FIRE AT WILL PILOT!", "", "", game.imageManager.get("guy1.png")) {
                          @Override
                          public Event chainedEvent(Game26 game) {
                            return new Event(0) {
                              @Override
                              public void start(Game26 game) {
                              }
    
                              @Override
                              public void end(Game26 game) {
                              }
    
                              @Override
                              public boolean isComplete(Game26 game) {
                                return dude.dead;
                              }
                            };
                          }
                        };
                      } 
                    };
                  }
                };
              }
            };
          }
        };
      }
      
    });
    
    events.add(new DialogEvent(1000, "ELIMINATING THEM APPEARS TO", "REVERSE THE MINIMALISATION.", "", game.imageManager.get("guy2.png")));
    events.add(new DialogEvent(0, "CONFIRMED. ENGAGE ALL APPROACHING", "HOSTILES.", "", game.imageManager.get("guy1.png")));
    events.add(new Event(5000) {
      private List<SquareDude> dudes;

      @Override
      public void start(Game26 game) {
        dudes = new ArrayList<SquareDude>();
        for(int i = 0; i < 5; i++) {
          dudes.add(game.addSquareDude(300 + (i * 100)));
        }
      }

      @Override
      public boolean isComplete(Game26 game) {
        return false;
      }
      
      @Override
      public void end(Game26 game) {
      }
    });
    
    currentEvent = events.get(currentEventIndex);
    nextEventTime = System.currentTimeMillis() + currentEvent.timeAfterPreviousEvent;
  }
  
  public void update(Game26 game) {
    if(currentEvent.isComplete(game)) {
      currentEvent.end(game);
      currentEventStarted = false;
      
      currentEvent = currentEvent.chainedEvent(game); 
      if(currentEvent == null) {
        currentEventIndex += 1;
        currentEvent = events.get(currentEventIndex);
      }
      
      nextEventTime = System.currentTimeMillis() + currentEvent.timeAfterPreviousEvent;
    }
    
    if(nextEventTime <= System.currentTimeMillis() && !currentEventStarted) {
      currentEvent.start(game);
      currentEventStarted = true;
    }
  }
  
  public class DialogEvent extends Event {
    long startTime;
    Image image;
    private String text1;
    private String text2;
    private String text3;

    public DialogEvent(long timeAfterPreviousEvent, String text1, String text2, String text3, Image image) {
      super(timeAfterPreviousEvent);
      this.image = image;
      this.text1 = text1;
      this.text2 = text2;
      this.text3 = text3;
    }

    @Override
    public void start(Game26 game) {
      startTime = System.currentTimeMillis();
      game.pauseMotion = true;
      game.dialog.image = image;
      game.dialog.text1 = text1;
      game.dialog.text2 = text2;
      game.dialog.text3 = text3;
      game.dialog.visible = true;
    }

    @Override
    public void end(Game26 game) {
      game.pauseMotion = false;
      game.dialog.visible = false;
    }

    @Override
    public boolean isComplete(Game26 game) {
      return (System.currentTimeMillis() - startTime) > 500 && game.input.isKeyDown(KeyEvent.VK_SPACE);
    }
    
  }
}

