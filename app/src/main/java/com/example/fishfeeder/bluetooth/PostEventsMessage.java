package com.example.fishfeeder.bluetooth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PostEventsMessage extends PostMessage {

    private static JSONObject constructMessage(List<FeedingEvent> eventList)
    {
        JSONObject obj = new JSONObject();
        try {
            JSONArray arr = new JSONArray();
            for(FeedingEvent ev : eventList)
            {
                JSONObject jsev = new JSONObject();
                jsev.put("startHour",ev.startHour);
                jsev.put("startMinute",ev.startMinute);
                jsev.put("durationSeconds",ev.durationSeconds);
                arr.put(jsev);
            }
            obj.put("events", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public PostEventsMessage(List<FeedingEvent> eventList) {
        super(constructMessage(eventList));
    }
}
