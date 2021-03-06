package pdp_ufrgs.opportunisticsensingprototype;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class BluetoothService extends Service {
    public static final String BROADCAST_ACTION = "pdp_ufrgs.opportunisticsensingprototype.BluetoothService";
    private int BT_THRESHOLD = 1;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private BluetoothAdapter btAdapter;
    private ArrayList<String> deviceList = new ArrayList<String>();
    double latitude, longitude = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) throws SecurityException {
        /* gets the bluetooth device threshold form main activity */
        this.BT_THRESHOLD = intent.getIntExtra("BT_THRESHOLD", 1);

        /* starts location listener */
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, locationListener);

        /* bluetooth adapter setting to start device discovery*/
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.startDiscovery();

        /* broadcast listener to detect nearby bluetooth devices */
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(btReceiver, filter);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(btReceiver);
        super.onDestroy();
    }


    /* BLUETOOTH BROADCAST RECEIVER */
    private final BroadcastReceiver btReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            /* captures a FOUND bluetooth broadcast -> new device */
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName() + "\n" + device.getAddress();
                boolean newDv = true;

                /* checks if device was already mapped */
                for(String d : deviceList) {
                    if(d.equals(deviceName)) {
                        newDv = false;
                        continue;
                    }
                }

                if(newDv) {
                    deviceList.add(device.getName() + "\n" + device.getAddress());
                }
                Log.d("BS/LIST: ", deviceList.toString());

            }
            /* captures a DISCOVERY_FINISHED broadcast -> end of search period (12s) */
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("BS/NUMBER OF: ", String.valueOf(deviceList.size()));

                if(deviceList.size() >= BT_THRESHOLD) {
                    Intent retIntent = new Intent(BROADCAST_ACTION);
                    SensingInfo sensorResult = new SensingInfo(latitude, longitude, deviceList, 0.0,
                                                                SensingInfo.BLUETOOTH_TYPE);
                    retIntent.putExtra("RESULT", sensorResult);
                    sendBroadcast(retIntent);
                    deviceList = new ArrayList<String>();
                }
                btAdapter.startDiscovery();
            }
        }
    };


    /* LOCATION SERVICE LISTENER */
    public class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc) {
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) throws SecurityException {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
            locationManager.removeUpdates(locationListener);
        }


        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(),"Status changed",Toast.LENGTH_SHORT).show();
        }

    }

}