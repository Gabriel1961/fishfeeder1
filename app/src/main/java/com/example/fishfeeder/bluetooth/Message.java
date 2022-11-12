package com.example.fishfeeder.bluetooth;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * this class represents a message that can be sent to a arduino board
 */
public class Message
{
    final JSONObject jsonMessage;
    final MessageType messageType;

    public Message(MessageType messageType, JSONObject jsonMessage)
    {
        // add type of message to json
        try {
            jsonMessage.put("type", messageType.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.jsonMessage = jsonMessage;
        this.messageType = messageType;
    }




    @Override
    public String toString()
    {
        return jsonMessage.toString();
    }
}
