package com.example.fishfeeder.bluetooth;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SyncTimeMessage extends Message{


    private static JSONObject constructMessage()
    {
        JSONObject obj = new JSONObject();
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                TimeZone tz = TimeZone.getDefault();
                int off = tz.getOffset(new Date().getTime()) / 1000;
                obj.put("epoch", Long.toString(Instant.now().getEpochSecond()+off));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public SyncTimeMessage() {
        super(MessageType.SYNC_TIME, constructMessage());
    }
}
