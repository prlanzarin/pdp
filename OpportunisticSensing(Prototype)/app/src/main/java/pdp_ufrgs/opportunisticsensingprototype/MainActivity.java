package pdp_ufrgs.opportunisticsensingprototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String IP;
    int PORT;
    private EditText btDevices, micIntensity, ipField, portField;
    private Switch btEnabled, micEnabled;
    private ArrayList<String> deviceList = new ArrayList<String>();
    double latitude, longitude, MIC_INTENSITY = 0.0;
    private Button connectButton;
    private int BLUETOOTH_DEVICES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // layout members
        this.btDevices = (EditText) findViewById(R.id.btDevices);
        this.micIntensity = (EditText) findViewById(R.id.micIntensity);
        this.ipField = (EditText) findViewById(R.id.ipField);
        this.portField = (EditText) findViewById(R.id.portField);
        this.connectButton = (Button) findViewById(R.id.connectButton);
        this.btEnabled = (Switch) findViewById(R.id.btEnabled);
        this.micEnabled = (Switch) findViewById(R.id.micEnabled);
    }

    /*
     * This method is called when CONNECT button is clicked
     */
    public void onConnect(View view) {
        boolean BLUETOOTH_ENABLED, MIC_ENABLED;
        String tmp;

        /* sensor parameters */
        tmp = btDevices.getText().toString();
        if(empty(tmp)) {
            this.btDevices.setError("Necessário");
            return;
        }
        this.BLUETOOTH_DEVICES = Integer.parseInt(tmp);

        tmp = micIntensity.getText().toString();
        if(empty(tmp)) {
            this.micIntensity.setError("Necessário");
            return;
        }
        this.MIC_INTENSITY = Double.parseDouble(tmp);

        /* switches */
        BLUETOOTH_ENABLED = this.btEnabled.isChecked();
        MIC_ENABLED = this.micEnabled.isChecked();

        /* IP and port fields */
        IP = this.ipField.getText().toString();
        if(empty(IP)) {
            this.ipField.setError("Endereço IP é necessário");
            return;
        }

        tmp = portField.getText().toString();
        if(empty(tmp)) {
            this.portField.setError("Porta é necessária");
            return;
        }
        PORT = Integer.parseInt(tmp);

        /* fire up the sensor services */
        this.setSensingService(BLUETOOTH_ENABLED, MIC_ENABLED);
    }

    /* Start sensor services  */
    public boolean setSensingService(boolean bt, boolean mic) {
        // starts connection thread. It captures the sensors' broadcasts and sends them to server
        Thread connectionThread = new Thread() {
            public void run() {
                Intent serviceIntent = new Intent(getApplicationContext(), ConnectionService.class);
                serviceIntent.putExtra("IP", IP);
                serviceIntent.putExtra("PORT", PORT);
                startService(serviceIntent);
            }
        };
        connectionThread.start();

        // bluetooth sensor service
        if(bt) {
            Thread btThread = new Thread() {
                public void run() {
                    Log.d("MAIN/BT: ", "STARTING SERVICE");
                    Intent serviceIntent = new Intent(getApplicationContext(), BluetoothService.class);
                    serviceIntent.putExtra("BT_THRESHOLD", BLUETOOTH_DEVICES);
                    startService(serviceIntent);
                }
            };
            btThread.start();
        }

        // microphone sensor service
        if(mic) {
            Thread micThread = new Thread() {
                public void run() {
                    Log.d("MAIN/MIC: ", "STARTING SERVICE");
                    Intent serviceIntent = new Intent(getApplicationContext(), MicrophoneService.class);
                    serviceIntent.putExtra("MIC_THRESHOLD", MIC_INTENSITY);
                    startService(serviceIntent);
                }
            };
            micThread.start();
        }

        return true;
    }

    public static boolean empty( final String s ) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

