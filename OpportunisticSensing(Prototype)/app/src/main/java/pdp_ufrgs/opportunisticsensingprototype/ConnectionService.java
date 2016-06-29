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

public class ConnectionService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) throws SecurityException {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothService.BROADCAST_ACTION);
        filter.addAction(MicrophoneService.BROADCAST_ACTION);
        registerReceiver(this.bcReceiver, filter);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(bcReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver bcReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("BROADCAST: ", "from" + action);

            if (action.equals(BluetoothService.BROADCAST_ACTION)) {
                SensingInfo sensorResult = (SensingInfo) intent.getSerializableExtra("RESULT");
                Log.d("BCAST RECV: ", sensorResult.deviceList.toString() + " "
                        + sensorResult.latitude + ", " + sensorResult.longitude);

            } else if (action.equals(MicrophoneService.BROADCAST_ACTION)) {

            }
        }
    };
}


