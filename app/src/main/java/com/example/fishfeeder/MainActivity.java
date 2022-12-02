package com.example.fishfeeder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.fishfeeder.bluetooth.BluetoothService;
import com.example.fishfeeder.bluetooth.FeedingEvent;
import com.example.fishfeeder.bluetooth.GetMessage;
import com.example.fishfeeder.bluetooth.PostEventsMessage;
import com.example.fishfeeder.bluetooth.SyncTimeMessage;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;


import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton notConnectedButton;
    private AppCompatButton connectedButton;
    private BluetoothService bluetooth;
    private NotificationsService notificationsService;
    public BluetoothService getBluetoohService() { return bluetooth; }
    private Handler messageHandler;
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("xxx","START");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT},1);
            }
        }
        messageHandler = new Handler();
        addConnectButtonHandler();

        bluetooth = new BluetoothService(Constants.hc05_classID,Constants.hc05_UUID);
        connectAsync(()->{
            bluetooth.sendMessage(new SyncTimeMessage());
            bluetooth.sendMessage(new GetMessage(new String[]{"temp"},(obj) -> Log.d("xxx",obj.toString())));
            bluetooth.sendMessage(new PostEventsMessage(Collections.singletonList(new FeedingEvent(21, 0, 5))));
            bluetooth.sendMessage(new GetMessage(new String[]{"temp", "temps"},(obj) -> Log.d("xxx",obj!=null ? obj.toString() : "")));
            //bluetooth.sendMessage(new GetMessage(new String[]{"temp"},(obj) -> Log.d("xxx",obj!=null ? obj.toString() : "")));
        });

        notificationsService = new NotificationsService(this);
        notificationsService.scheduleAlarm();
    }

    /**
     * Connects to the bluetooth device asyncronously
     */
    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private void connectAsync(Runnable runOnDone)
    {
        ScheduledExecutorService ex = new ScheduledThreadPoolExecutor(1);
        // add handler to change the state of the connected button if the connection to the bluetooth device succeeds
        bluetooth.connectionChangedEvents.add((con)-> this.runOnUiThread(()->{
            if(con)
            {
                notConnectedButton.setVisibility(View.GONE);
                connectedButton.setVisibility(View.VISIBLE);
            }
            else
            {
                notConnectedButton.setVisibility(View.VISIBLE);
                connectedButton.setVisibility(View.GONE);
            }
        }));

        ex.execute(() -> {
            this.bluetooth.connect();
            runOnDone.run();
        });

    }

    /**
     * adds functionality to the connect button on the upper right side of the main app screen
     */
    private void addConnectButtonHandler()
    {
        connectedButton = findViewById(R.id.connectButton);
        notConnectedButton = findViewById(R.id.notConnectButton);
        notConnectedButton.setOnClickListener(v -> {
            if (!bluetooth.isConnected())
            {
                //redirect to bluetoothmenu
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       bluetooth.closeConnection();
    }
}