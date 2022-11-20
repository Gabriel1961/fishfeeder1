package com.example.fishfeeder.bluetooth;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SyncTimeMessage extends Message{

    private static JSONObject constructMessage()
    {
        JSONObject obj = new JSONObject();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            try {
                obj.put("epoch", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }


    public SyncTimeMessage() {
        super(MessageType.SYNC_TIME, constructMessage());
    }
}
