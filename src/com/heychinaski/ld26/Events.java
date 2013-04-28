package com.heychinaski.ld26;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Events {
  public List<Event> events = new ArrayList<Event>();
  
  public Event currentEvent;
  public int currentEventIndex = 0;
  
  public long nextEventTime = 0;
  public boolean currentEventStarted = false;
  
  public Random pseudoRand = new Random(0xDEADBEEF);
  
  public Event gameOverEvent;
  
  public Events(Game26 game, int eventIndex) {
    currentEventIndex = eventIndex;
    gameOverEvent = new DialogEvent(0, "YOU DIED!", "", "<PRESS N TO CONTINUE>", game.imageManager.get("guy1.png"), true) {
      public void end(Game26 game) {
        super.end(game);
        game.init(currentEventIndex, game.player.fireFreq);
      };
    };
    events.add(new DialogEvent(2000, "YOU REMEMBER HOW TO FLY", "THESE THINGS?", "<PUSH N TO CONTINUE>", game.imageManager.get("guy1.png"), false));
    events.add(new DialogEvent(0, "OF COURSE BUT WHY DON'T YOU", "REFRESH MY MEMORY?", "", game.imageManager.get("guy2.png"), false));
    events.add(new DialogEvent(0, "USE THE UP AND DOWN ARROW", "KEYS TO CONTROL YOUR SHIP.", "", game.imageManager.get("guy1.png"), false));
    events.add(new DialogEvent(2000, "LEFT AND RIGHT WILL CONTROL", "YOUR SPEED.", "", game.imageManager.get("guy1.png"), false));
    events.add(new DialogEvent(2000, "TAKE IT EASY OUT THERE PILOT.", "", "", game.imageManager.get("guy1.png"), false));
    events.add(new DialogEvent(0, "WILL DO COMMAND. SHOULD BE A", "ROUTINE MISSION...", "", game.imageManager.get("guy2.png"), false));
    events.add(new Event(3000) {
      private BadDude dude;

      @Override
      public void start(Game26 game) {
        dude = game.addSquareDude();
      }

      @Override
      public boolean isComplete(Game26 game) {
        return dude != null 
            && (game.ourCamera.worldXToScreenX(dude.x) < game.getWidth() - 100);
      }
      
      @Override
      public void end(Game26 game) {
      }
      
      @Override
      public DialogEvent chainedEvent(Game26 game) {
        return new DialogEvent(0, "HOLY POTATOES! WHAT IS THAT?", "", "", game.imageManager.get("guy2.png"), true) {
          @Override
          public Event chainedEvent(Game26 game) {
            return new DialogEvent(0, "UNKNOWN. AND IT APPEARS TO BE", "AFFECTING THE SPACE TIME AROUND IT", "", game.imageManager.get("guy1.png"), true) {
              @Override
              public Event chainedEvent(Game26 game) {
                return new DialogEvent(0, "APPEARS HOSTILE. PERMISSION TO", "ENGAGE?", "", game.imageManager.get("guy2.png"), true) {
                  @Override
                  public Event chainedEvent(Game26 game) {
                    return new DialogEvent(0, "IDENTIFIED HOSTILE AS A", "MINIMALISOR. THEY MINIMALISE", "EVERYTHING AROUND THEM.", game.imageManager.get("guy1.png"), true) {
                      @Override
                      public Event chainedEvent(Game26 game) {
                        return new DialogEvent(0, "FIRE AT WILL PILOT!", "", "USE THE SPACEBAR TO FIRE.", game.imageManager.get("guy1.png"), true) {
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
    
    events.add(new DialogEvent(1000, "ELIMINATING THEM APPEARS TO", "REVERSE THE MINIMALISATION.", "", game.imageManager.get("guy2.png"), false));
    events.add(new DialogEvent(1000, "CONFIRMED. ENGAGE ALL APPROACHING", "HOSTILES.", "", game.imageManager.get("guy1.png"), false));
    events.add(new BadDudeAddingEvent(3000) {
      @Override
      public void addDudes(Game26 game) {
        for(int i = 0; i < 5; i++) {
          dudes.add(game.addSquareDude(300 + (i * pseudoRand.nextInt(200))));
        }        
      }
      
    });
    events.add(new BadDudeAddingEvent(1000)  {
      @Override
      public void addDudes(Game26 game) {
        for(int i = 0; i < 10; i++) {
          dudes.add(game.addSquareDude(300 + (i * pseudoRand.nextInt(200))));
        }        
      }
      
    });
    events.add(new BadDudeAddingEvent(1000)  {
      @Override
      public void addDudes(Game26 game) {
          dudes.add(game.addRoundDude(300, 0, 2, 100));
      }
      
    });
    events.add(new BadDudeAddingEvent(1000)  {
      @Override
      public void addDudes(Game26 game) {
        for(int i = 0; i < 5; i++) {
          dudes.add(game.addSquareDude(300 + (i * pseudoRand.nextInt(100))));
        }
        
        for(int i = 0; i < 10; i++) {
          dudes.add(game.addRoundDude(500 + (i * 5), 50, 3, 75));
        }
      }
    });
    events.add(new BadDudeAddingEvent(1000)  {
      @Override
      public void addDudes(Game26 game) {
        for(int i = 0; i < 5; i++) {
          dudes.add(game.addSquareDude(300 + (i * pseudoRand.nextInt(100))));
        }
        
        for(int i = 0; i < 5; i++) {
          dudes.add(game.addRoundDude(300 + (i * 5), -50, 4, 80));
        }
        
        for(int i = 0; i < 5; i++) {
          dudes.add(game.addRoundDude(800 + (i * 5), 50, 5, 50));
        }
      }
    });
    
    events.add(new BadDudeAddingEvent(1000)  {
      @Override
      public void addDudes(Game26 game) {
        TriDude previousDude = null;
        for(int i = 0; i < 1; i++) {
          previousDude = game.addTriDude(300 + (i * 50), -100, previousDude);
          dudes.add(previousDude);
        }
      }
    });
    
    events.add(new DialogEvent(2000, "LOOKS LIKE YOU'RE GOING TO", "NEED MORE FIREPOWER!", "", game.imageManager.get("guy1.png"), false));
    events.add(new Event(0) {
      
      PowerUp p;
      
      @Override
      public void start(Game26 game) {
        p = game.addPowerUp(300);
      }
      
      @Override
      public void end(Game26 game) {
        game.player.fireFreq= 250; 
      }
      
      @Override
      public boolean isComplete(Game26 game) {
        return p != null && p.collected;
      }
    });
    events.add(new BadDudeAddingEvent(1000)  {
      @Override
      public void addDudes(Game26 game) {
        for(int i = 0; i < 30; i++) {
          dudes.add(game.addSquareDude(300 + (i * pseudoRand.nextInt(50))));
        }
      }
    });
    
    events.add(new BadDudeAddingEvent(1000)  {
      @Override
      public void addDudes(Game26 game) {
        TriDude previousDude = null;
        for(int i = 0; i < 5; i++) {
          previousDude = game.addTriDude(300 + (i * 50), -100, previousDude);
          dudes.add(previousDude);
        }
      }
    });
    events.add(new DialogEvent(0, "I GUESS I CAN BE USED FOR", "DEBUGGING TOO!", "", game.imageManager.get("guy1.png"), true));
    
    currentEvent = events.get(currentEventIndex);
    nextEventTime = System.currentTimeMillis() + currentEvent.timeAfterPreviousEvent;
  }
  
  public void update(Game26 game) {
    if(currentEventStarted && currentEvent.isComplete(game)) {
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
  
  public void gameOver(Game26 game) {
    if(currentEvent != gameOverEvent) {
      currentEvent = gameOverEvent;
      currentEventStarted = false;
    }
  }
  
  public class DialogEvent extends Event {
    long startTime;
    Image image;
    private String text1;
    private String text2;
    private String text3;
    private boolean pausesAction;

    public DialogEvent(long timeAfterPreviousEvent, String text1, String text2, String text3, Image image, boolean pausesAction) {
      super(timeAfterPreviousEvent);
      this.image = image;
      this.text1 = text1;
      this.text2 = text2;
      this.text3 = text3;
      this.pausesAction = pausesAction;
    }

    @Override
    public void start(Game26 game) {
      startTime = System.currentTimeMillis();
      game.pauseMotion = pausesAction;
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
      game.input.keyUp(KeyEvent.VK_N);
    }

    @Override
    public boolean isComplete(Game26 game) {
      return (System.currentTimeMillis() - startTime) > 500 && game.input.isKeyDown(KeyEvent.VK_N);
    }
  }
    
  public abstract class BadDudeAddingEvent extends Event {

    public BadDudeAddingEvent(long timeAfterPreviousEvent) {
      super(timeAfterPreviousEvent);
    }

    protected List<BadDude> dudes;

    @Override
    public void start(Game26 game) {
      dudes = new ArrayList<BadDude>();
      addDudes(game);
    }
    
    public abstract void addDudes(Game26 game);

    @Override
    public boolean isComplete(Game26 game) {
      boolean complete = true;
      for(int i = 0; i < dudes.size(); i++) {
        complete &= dudes.get(i).dead;
      }
      return complete;
    }
    
    @Override
    public void end(Game26 game) {
      dudes = null;
    }
    
  }
}

