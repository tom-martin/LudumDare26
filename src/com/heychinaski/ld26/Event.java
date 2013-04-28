package com.heychinaski.ld26;

public abstract class Event {
  long timeAfterPreviousEvent;
  
  public Event(long timeAfterPreviousEvent) {
    super();
    this.timeAfterPreviousEvent = timeAfterPreviousEvent;
  }
  
  public abstract void start(Game26 game);
  public abstract void end(Game26 game);
  public abstract boolean isComplete(Game26 game);
  
  public Event chainedEvent(Game26 game) {return null;}
}
