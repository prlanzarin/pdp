package pdp_ufrgs.opportunisticsensingprototype;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private boolean BLUETOOTH_ENABLED, LOCATION_ENABLED, MIC_ENABLED = false;
    private EditText btDevices, micIntensity, ipField, portField;
    private Switch btEnabled, micEnabled, locationEnabled;
    private Button connectButton;
    private int BLUETOOTH_DEVICES = 1, MIC_INTENSITY = 0;

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
        this.locationEnabled = (Switch) findViewById(R.id.locationEnabled);
    }

    /*
     * This method is called when CONNECT button is clicked
     */
    public void onConnect(View view) {
        String ip, tmp;
        int port;

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
        this.MIC_INTENSITY = Integer.parseInt(tmp);

        /* switches */
        this.BLUETOOTH_ENABLED = this.btEnabled.isChecked();
        this.LOCATION_ENABLED = this.locationEnabled.isChecked();
        this.MIC_ENABLED = this.micEnabled.isChecked();

        /* IP and port fields */
        ip = this.ipField.getText().toString();
        if(empty(ip)) {
            this.ipField.setError("Endereço IP é necessário");
            return;
        }

        tmp = portField.getText().toString();
        if(empty(tmp)) {
            this.portField.setError("Porta é necessária");
            return;
        }
        port = Integer.parseInt(tmp);

        /* connect to server */
        // TODO

        /* do services stuff */
        this.setSensingService(this.BLUETOOTH_ENABLED, this.MIC_ENABLED, this.LOCATION_ENABLED);
    }

    /* TODO */
    public boolean setSensingService(boolean bt, boolean mic, boolean gps) {
        Intent serviceIntent = new Intent(this, SensingService.class);
        startService(serviceIntent);
        return false;
    }

    public static boolean empty( final String s ) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

