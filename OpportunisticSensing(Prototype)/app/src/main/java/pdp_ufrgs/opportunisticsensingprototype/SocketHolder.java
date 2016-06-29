package pdp_ufrgs.opportunisticsensingprototype;

import android.app.ExpandableListActivity;
import android.hardware.SensorEvent;
import android.provider.SyncStateContract;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ResourceBundle;

/**
 * Created by Usuário on 17/11/2015.
 */
public class SocketHolder {

    private static Socket clientSocket = null;
    private static ObjectOutputStream outStream = null;
    private static ObjectInputStream inStream = null;

    public static Socket getSocket(){
        return clientSocket;
    }

    public static void setSocket(Socket s) throws Exception {
        clientSocket = s;
        try {
            outStream = new ObjectOutputStream(s.getOutputStream());
            inStream = new ObjectInputStream(s.getInputStream());
        }catch(Exception e)
        {
            throw e;
        }
    }

    public static void writeMessage(SensingInfo m) throws Exception
    {
        try{
            outStream.writeObject(m);
            outStream.flush();
        }catch(Exception e)
        {
            throw e;
        }
    }

    public static void closeSocket(){

        SensingInfo m = new SensingInfo(0.0, 0.0, null, 0.0, "CLOSE");

        try {
            writeMessage(m);
            clientSocket.close();
        }catch(Exception e){}

        clearSocket();
    }

    public static void clearSocket(){
        clientSocket = null;
        inStream = null;
        outStream = null;
    }
}
