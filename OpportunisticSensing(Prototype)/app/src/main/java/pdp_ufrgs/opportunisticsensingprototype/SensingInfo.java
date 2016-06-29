package pdp_ufrgs.opportunisticsensingprototype;

import java.io.Serializable;
import java.util.ArrayList;

public class SensingInfo implements Serializable {
    public static final String BLUETOOTH_TYPE = "BLUETOOTH";
    public static final String MICROPHONE_TYPE = "MICROPHONE";
    public static final String CONNECTION_CLOSE = "CLOSE";
    public double latitude, longitude;
    public ArrayList<String> deviceList = null;
    public double micIntensity;
    public String type = null;

    /* A serializable object to pass between server and app with sensor info */
    SensingInfo(double lat, double longi, ArrayList<String> dl, double mic, String type) {
        this.latitude = lat;
        this.longitude = longi;
        this.deviceList = dl;
        this.micIntensity = mic;
        this.type = type;
    }
}
