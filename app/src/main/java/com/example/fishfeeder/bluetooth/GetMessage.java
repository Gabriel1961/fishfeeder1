package com.example.fishfeeder.bluetooth;

import android.content.Context;

import com.example.fishfeeder.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetMessage extends Message{

    public interface ResponseEvent{
        void run(JSONObject obj);
    }

    public ResponseEvent responseEvent;

    private static JSONObject constructMessage(String[] fields)
    {
        JSONObject jsonMessage = new JSONObject();
        try {
            JSONArray ar = new JSONArray(fields);
            jsonMessage.put("get",ar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonMessage;
    }

    public GetMessage(String[] fields, ResponseEvent responseEvent) {
        super(MessageType.GET, constructMessage(fields));
        this.responseEvent = responseEvent;
    }

}
