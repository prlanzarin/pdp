package pdp_ufrgs.opportunisticsensingprototype;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MicrophoneService extends Service {
    public static final String BROADCAST_ACTION = "pdp_ufrgs.opportunisticsensingprototype.MicrophoneService";
    private MediaRecorder mRecorder = null;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    double latitude, longitude = 0;
    double MIC_THRESHOLD = 0;
    Thread runningThread;
    boolean THREAD_RUNNING = false;

    @Override
    public void onCreate() throws SecurityException{
        super.onCreate();

        Log.d("MC/FL: ", "MIC START");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, locationListener);

        runningThread = new Thread() {
            @Override
            public void run() {
                try {
                    while(THREAD_RUNNING) {
                        Thread.sleep(1000);
                        double db = 20 * Math.log(mRecorder.getMaxAmplitude() / 2700.0);
                        Log.d("MC/DB: ", String.valueOf(db));
                        Log.d("MC/AM: ", String.valueOf(mRecorder.getMaxAmplitude()));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        startRecorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        THREAD_RUNNING = false;
        stopRecorder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) throws SecurityException {
        /* gets the mic device threshold form main activity */
        this.MIC_THRESHOLD = intent.getIntExtra("MIC_THRESHOLD", 1);

        THREAD_RUNNING = true;
        runningThread.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            try {
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;

    }

    public class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc) {
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) throws SecurityException {
            Toast.makeText(getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT).show();
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