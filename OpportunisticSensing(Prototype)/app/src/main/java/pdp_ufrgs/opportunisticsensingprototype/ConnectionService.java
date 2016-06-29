package pdp_ufrgs.opportunisticsensingprototype;

import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConnectionService extends Service {
    String IP = "127.0.1.1";
    int PORT = 26789;
    Socket socket;
    boolean socketIsConnected = false;
    boolean finishedConnection = false;

    @Override
    public void onCreate() throws SecurityException {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothService.BROADCAST_ACTION);
        filter.addAction(MicrophoneService.BROADCAST_ACTION);
        registerReceiver(this.bcReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) throws SecurityException {

        this.IP = intent.getStringExtra("IP");
        this.PORT = intent.getIntExtra("PORT", 26789);

        Log.d("CONNECTION TO: ", IP + " at " + PORT);
        setTcpConnection(this.IP);


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

        /* closing connection */
        SensingInfo closer = new SensingInfo(0.0, 0.0, null, 0.0, SensingInfo.CONNECTION_CLOSE);
        try{ SocketHolder.writeMessage(closer); } catch(Exception e) {System.out.println(e.toString());}
        SocketHolder.closeSocket();

        super.onDestroy();
    }

    private BroadcastReceiver bcReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("BROADCAST: ", "from" + action);

            /* if service received a bluetooth sensor result broadcast, sends it to server */
            if (action.equals(BluetoothService.BROADCAST_ACTION)) {
                SensingInfo sensorResult = (SensingInfo) intent.getSerializableExtra("RESULT");
                Log.d("BCAST RECV: ", sensorResult.deviceList.toString() + " "
                        + sensorResult.latitude + ", " + sensorResult.longitude);
                if(socketIsConnected)
                    try{SocketHolder.writeMessage(sensorResult);}catch(Exception e){System.out.println(e.toString());}

            }
            /* if service received a microphone sensor result broadcast, sends it to server */
            else if (action.equals(MicrophoneService.BROADCAST_ACTION)) {
                SensingInfo sensorResult = (SensingInfo) intent.getSerializableExtra("RESULT");
                Log.d("BCAST RECV: ", sensorResult.micIntensity + " "
                        + sensorResult.latitude + ", " + sensorResult.longitude);
                if(socketIsConnected)
                    try{SocketHolder.writeMessage(sensorResult);}catch(Exception e){System.out.println(e.toString());}
            }
        }
    };

    /* sets a TCP connection to IP:this.PORT */
    public void setTcpConnection(final String ip) {
        this.socket = new Socket();
        this.socketIsConnected = false;
        this.finishedConnection = false;

        Thread ti = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    socket.connect(new InetSocketAddress(ip, PORT));
                    socketIsConnected = true;
                }catch(Exception e)
                {
                    System.out.println(e.toString());
                    socketIsConnected = false;
                }
                finishedConnection = true;
            }
        });

        ti.start();

        while(!this.finishedConnection){
            try{
                Thread.sleep(500);
            }catch(Exception e){}
        }

        try {
            SocketHolder.setSocket(socket);
        }catch (Exception e) {}
    }

}


