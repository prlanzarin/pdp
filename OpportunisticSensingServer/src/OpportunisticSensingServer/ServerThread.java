/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OpportunisticSensingServer;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.awt.Robot;
/**
 *
 * @author Claudio Gisch e Paulo Lanzarin
 */
public class ServerThread extends Thread {
    
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private MainWindow2 window = null;
    private static boolean commands[] = new boolean[12];
    private static boolean isPressed[] = new boolean[12];
    private static boolean cancel = false;
    
    /**
     * Creates a ServerSocket. Throws Exception in case of error.
     * @param serverPort
     * @throws Exception 
     */
    ServerThread(int serverPort) throws Exception
    {
        try { 
            serverSocket = new ServerSocket(serverPort, 2, InetAddress.getLocalHost());
            System.out.print("Server address:  "+serverSocket.toString());
            for(int i=0;i<12;i++){
                commands[i] = false;
                isPressed[i] = false;
            }
        }catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     * Waits for client to connect. Throws Exception in case of error.
     * @throws Exception 
     */
    public void acceptClient() throws Exception
    {
        try{
            clientSocket = serverSocket.accept();
        }catch(Exception e)
        {
            throw e;
        }
    }
    
    
    @Override
    public void run()
    {
        SensingInfo message;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        this.window.setStatusText("Conectado");
        try{
            out = new ObjectOutputStream(this.clientSocket.getOutputStream());
        }catch(Exception e)
        {
            this.window.appendTextInfoArea(e.toString());
        }
        
        Thread ct = new Thread();
        ct.start();
        
        try{
            in = new ObjectInputStream(this.clientSocket.getInputStream()); 
            SensingInfo m;
            
            while(!this.window.isConnectionCanceled())
            {
                /*Receive message*/  
                m = (SensingInfo)in.readObject();
                if(m.type.equals(m.MICROPHONE_TYPE))
                {
                     this.window.appendTextInfoArea("Localização:");
                     this.window.appendTextInfoArea("Latitude - "+m.latitude);
                     this.window.appendTextInfoArea("Longitude - "+m.longitude);
                     this.window.appendTextInfoArea("Amplitude de som detectado: "+m.longitude);
                     this.window.appendTextInfoArea("\n\n");
                }
                else if(m.type.equals(m.BLUETOOTH_TYPE))
                {
                    this.window.appendTextInfoArea("Localização:");
                    this.window.appendTextInfoArea("Latitude - "+m.latitude);
                    this.window.appendTextInfoArea("Longitude - "+m.longitude);
                    this.window.appendTextInfoArea("Identificação dos dispositivos bluetooth encontrados:");
                    for(String id : m.deviceList){
                        this.window.appendTextInfoArea(id);
                    }
                    this.window.appendTextInfoArea("\n\n");
                }
                else {
                    //closes connection
                }
            }
            
            cancel = true;
            
        }catch(Exception e)
        {
            
        }
        
    }
    
    public String getServerAddress()
    {
        return serverSocket.toString();
    }
    
    public String getServerPort()
    {
        return Integer.toString(serverSocket.getLocalPort());
    }
    
    public String getClientAddress()
    {
        return clientSocket.toString();
    }
    
    public void setWindow(MainWindow2 window)
    {
        this.window = window;
    }
    
    public MainWindow2 getWindow()
    {
        return this.window;
    }
    
}