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

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;


import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    private boolean isConnected;
    public void setIsConnected(boolean value)
    {
        isConnected = value;
        if(isConnected)
        {
            notConnectedButton.setVisibility(View.GONE);
            connectedButton.setVisibility(View.VISIBLE);
        }
        else
        {
            notConnectedButton.setVisibility(View.VISIBLE);
            connectedButton.setVisibility(View.GONE);
        }
    }
    private AppCompatButton notConnectedButton;
    private AppCompatButton connectedButton;
    private BluetoothService bluetooth;

    public BluetoothService getBluetoohService() { return bluetooth; }

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


        addConnectButtonHandler();

        bluetooth = new BluetoothService(Constants.hc05_classID,Constants.hc05_UUID);
        connectAsync();

        bluetooth.sendMessage(new SyncTimeMessage());
        bluetooth.sendMessage(new GetMessage(new String[]{"temp"},(obj) -> Log.d("xxx",obj.toString())));
        bluetooth.sendMessage(new PostEventsMessage(Collections.singletonList(new FeedingEvent(21, 0, 5))));

        NotificationsService ns = new NotificationsService(this);
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private void connectAsync()
    {
        ScheduledExecutorService ex = new ScheduledThreadPoolExecutor(1);
        ex.execute(() -> {
            boolean res = this.bluetooth.connect();
            this.runOnUiThread(()->this.setIsConnected(res));
        });
    }

    private void addConnectButtonHandler()
    {
        connectedButton = findViewById(R.id.connectButton);
        notConnectedButton = findViewById(R.id.notConnectButton);
        notConnectedButton.setOnClickListener(v -> {
            if (!isConnected)
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