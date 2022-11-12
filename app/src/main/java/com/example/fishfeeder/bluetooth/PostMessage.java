package com.example.fishfeeder.bluetooth;

import org.json.JSONObject;

import java.util.HashMap;

public class PostMessage extends Message{

    public PostMessage(HashMap<String,Object> kvp) {
        super(MessageType.POST, new JSONObject(kvp));
    }
}
