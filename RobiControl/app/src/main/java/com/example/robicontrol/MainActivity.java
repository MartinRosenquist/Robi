package com.example.robicontrol;


import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


//Gatt/Gap = Peripheral
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    Activity MainActivity = MainActivity.this;
    private BluetoothAdapter _adapter = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> _bondedDevices = _adapter.getBondedDevices();
    private BluetoothSocket _socket;
    private BluetoothDevice _device;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private ArrayList<BluetoothDevice> _ArrayDevice = new ArrayList<>();




    private BroadcastReceiver _btReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String _action = intent.getAction();

            if (_action.equals(_adapter.ACTION_STATE_CHANGED)){
                final int _state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, _adapter.ERROR);

                switch (_state){
                    case BluetoothAdapter.STATE_ON:
                        PrintText(MainActivity,"Bluetooth is On", 0);
                        break; 
                    case BluetoothAdapter.STATE_OFF:
                        PrintText(MainActivity,"Bluetooth is Off", 0);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        PrintText(MainActivity,"Bluetooth is Turning Off", 0);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        PrintText(MainActivity,"Bluetooth is Turning On", 0);
                        break;
                }
            }
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!_adapter.isEnabled()){
            Intent _enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(_enableBT);

            IntentFilter _filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(_btReceiver, _filter);
        }
        else {
            _device = GetBondedDevice("poke");
        }



      PrintText(MainActivity, "Device Name: " + _device.getName() + "r\nDevice Address: " + _device.getAddress(), 1);

    }

    // on app closing
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(_btReceiver);
    }


    //Checking if you are using an outdated Android Version, otherwise ask for Location permissions
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

    public void PrintText(Activity _activity, String _text, int _duration){
        Toast.makeText(_activity, _text, _duration).show();
    }

    /*
        Get the specific bonded device
    */
    private BluetoothDevice GetBondedDevice(String _name){
        BluetoothDevice _result = null;
        if (_bondedDevices != null){
            for (BluetoothDevice _device : _bondedDevices){
                if (_device.getName().contains(_name)){
                    _result = _device;
                }
            }
        }


        return _result;
    }


}
