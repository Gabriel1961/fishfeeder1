package com.example.fishfeeder;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.fishfeeder.bluetooth.BluetoothService;
import com.example.fishfeeder.bluetooth.GetMessage;
import com.example.fishfeeder.bluetooth.Message;
import com.example.fishfeeder.bluetooth.SyncTimeMessage;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("xxx","START");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT},1);
            }
            return;
        }

        BluetoothService service = new BluetoothService(Constants.hc05_classID,Constants.hc05_UUID);
        service.connect();
        service.sendMessage(new SyncTimeMessage());
        service.sendMessage(new GetMessage(new String[]{"temp"},(obj) -> Log.d("xxx",obj.toString())));
    }

}