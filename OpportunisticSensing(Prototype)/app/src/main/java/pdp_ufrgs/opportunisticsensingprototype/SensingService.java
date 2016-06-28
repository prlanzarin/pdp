package pdp_ufrgs.opportunisticsensingprototype;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;

public class SensingService extends Service {
    private static final String DEBUG_TAG = "SensingService";

    private BluetoothAdapter btAdapter;
    private MicrophoneSensor micSensor;

    public SensingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.micSensor = new MicrophoneSensor();

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(btReceiver, filter);


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(btReceiver);
        super.onDestroy();
    }

    private final BroadcastReceiver btReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("BT FOUND: ", device.getName() + "\n" + device.getAddress());
            }
        }
    };
}