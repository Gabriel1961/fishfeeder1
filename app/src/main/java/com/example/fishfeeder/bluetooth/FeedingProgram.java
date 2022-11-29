package com.example.fishfeeder.bluetooth;

import java.util.ArrayList;
import java.util.Collections;

public class FeedingProgram {
    public FeedingQtyType feedingQtyType;
    public FeedingFrequencyType feedingFrequencyType;
    public int nrOfFishes;

    public FeedingProgram(FeedingQtyType feedingQtyType,FeedingFrequencyType feedingFrequencyType,int nrOfFishes)
    {
        this.feedingFrequencyType = feedingFrequencyType;
        this.feedingQtyType = feedingQtyType;
        this.nrOfFishes = nrOfFishes;
    }

    public Message getMessage() {
        ArrayList<FeedingEvent> ev = new ArrayList<FeedingEvent>();
        int duration = (feedingQtyType.getValue()+1) * nrOfFishes * 2; // TODO change base duration
        switch (feedingFrequencyType)
        {
            case ONE_TIME:
                ev.add(new FeedingEvent(16,0,duration));
                break;

            case TWO_TIMES:
                ev.add(new FeedingEvent(10,0,duration));
                ev.add(new FeedingEvent(18,0,duration));
                break;

            case THREE_TIMES:
                ev.add(new FeedingEvent(10,0,duration));
                ev.add(new FeedingEvent(16,0,duration));
                ev.add(new FeedingEvent(20,0,duration));
                break;
        }

        return new PostEventsMessage(ev);
    }
}
