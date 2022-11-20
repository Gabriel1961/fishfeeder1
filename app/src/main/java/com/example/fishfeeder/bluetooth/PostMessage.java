package com.example.fishfeeder.bluetooth;

import org.json.JSONObject;

import java.util.HashMap;

public class PostMessage extends Message{

    public PostMessage(JSONObject post) {
        super(MessageType.POST, post);
    }
}
