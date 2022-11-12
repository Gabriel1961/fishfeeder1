package com.example.fishfeeder.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import com.example.fishfeeder.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;


public class BluetoothService {
    BluetoothAdapter adapter;
    BluetoothDevice device;
    BluetoothSocket socket;
    OutputStream output;
    InputStream input;
    UUID deviceUUID;
    Handler responseHandler;
    /**
     * this event runs whenever a message is received (it is called from the backround thread)
     * so if ui or viewmodel changes are necessary, you need to invoke them using MainActivity::runOnUiThread
     */

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    public BluetoothService(String deviceClassCode, UUID deviceUUID) {
        adapter = BluetoothAdapter.getDefaultAdapter();
        this.deviceUUID = deviceUUID;

        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for(BluetoothDevice d : devices)
            if(d.getBluetoothClass().toString().equals(deviceClassCode))
                device = d;
        responseHandler = new Handler();
        if(device == null)
            Log.e("xxx","device cannot be found");
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    public void connect()
    {
        // try to connect multiple times to the device
        int maxTries = 10, tries = 0;
        do
        {
            try {
                socket = this.device.createRfcommSocketToServiceRecord(deviceUUID);
                socket.connect();

            } catch (IOException e) {
                Log.e("xxx","", e);
            }
            tries++;
        }while (!socket.isConnected() && tries < maxTries);

        // Create output and input streams

        try {
            output = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("xxx","", e);
        }

        try {
            input = socket.getInputStream();

        } catch (IOException e) {
            Log.e("xxx","", e);
        }
        Log.d("xxx","connected to device");
    }

    public void sendMessage(Message message)
    {
        if(output == null )
            return;
        try {
            // send message
            output.write(message.toString().getBytes(StandardCharsets.UTF_8));
            Log.d("xxx","sending message: " + message.toString());

            if(message instanceof GetMessage) // schedule task to read the response in the future
            {
                GetMessage gmsg = (GetMessage) message;
                responseHandler.postDelayed(()-> {
                    JSONObject obj = this.read();
                    gmsg.responseEvent.run(obj);
                },1000);
            }
        } catch (IOException e) {
            Log.e("xxx","", e);
        }
    }


    /**
     * this function is called with a delay after sending a get message to the device
     *
     */
    public JSONObject read() {
        Scanner sc = new Scanner(input);
        if(sc.hasNext())
        {
            String r = sc.nextLine();
            JSONObject obj = null;
            try {
                obj = new JSONObject(r);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if(obj != null)
                    Log.e("xxx", obj.getString("err"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }
        return null;
    }


    public void closeConnection()
    {
        if(socket != null)
        {
            try {
                output.close();
                socket.close();
                output = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
