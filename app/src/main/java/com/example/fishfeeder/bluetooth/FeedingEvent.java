package com.example.fishfeeder.bluetooth;

public class FeedingEvent {
    public FeedingEvent(int startHour,int startMinute,int durationSeconds)
    {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.durationSeconds = durationSeconds;
    }
    public int startHour, startMinute, durationSeconds;
}
