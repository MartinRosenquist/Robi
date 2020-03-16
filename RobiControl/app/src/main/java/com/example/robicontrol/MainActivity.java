package com.example.robicontrol;


import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.app.ListActivity;

import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private OutputStream outputStream;
    private InputStream inStream;
    public ArrayList<BluetoothDevice> _btDevices = new ArrayList<>();

    private final BluetoothManager _bluetoothManager;

    {
        _bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    }

    private final BluetoothAdapter _bluetoothAdapter = _bluetoothManager.getAdapter();


    public Boolean onOff = false;
    private BroadcastReceiver _broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice _device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (_device.getName().contains("ZBC-PAA")) {
                    _btDevices.add(_device);
                    Toast.makeText(MainActivity.this, _device.getName(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        if ((_bluetoothAdapter == null) || !_bluetoothAdapter.isEnabled()) {
            Intent _enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(_enableBT);

        }

        if (!_bluetoothAdapter.isDiscovering()) {

            checkBTPermission();

            _bluetoothAdapter.startDiscovery();

            IntentFilter _Discoverdevices = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(_broadcastreceiver, _Discoverdevices);
        } else {
            _bluetoothAdapter.cancelDiscovery();

            _bluetoothAdapter.startDiscovery();

            IntentFilter _Discoverdevices = new IntentFilter(BluetoothDevice.ACTION_FOUND);

            checkBTPermission();

            registerReceiver(_broadcastreceiver, _Discoverdevices);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(_broadcastreceiver);
    }

    @SuppressLint("ObsoleteSdkInt")
    private void checkBTPermission() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkCallingOrSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkCallingOrSelfPermission("Manifest.Permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }
        }
    }


}
/*
    Set<BluetoothDevice> bondedDevices = _bluetoothAdapter.getBondedDevices();

                if (bondedDevices.size() > 0) {

                        for (BluetoothDevice _device : bondedDevices) {
                        if (_device.getName() == "ZBC-PAALION0005") {
                        Toast.makeText(MainActivity.this, _device.getName(), Toast.LENGTH_LONG).show();
                        }

                        }

                        }

                        Log.e("error", "No appropriate paired devices.");
                        } else {
                        Log.e("error", "Bluetooth is disabled.");
                        }*/