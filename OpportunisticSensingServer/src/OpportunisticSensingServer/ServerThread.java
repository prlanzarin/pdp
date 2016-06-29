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
 * @author Usuário
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
        ControlMessage message = new ControlMessage();
        message.setCode(Constants.INITIATE_COMMUNICATION_CODE);
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        this.window.setStatusText("Conectado");
        try{
            out = new ObjectOutputStream(this.clientSocket.getOutputStream());
            out.writeObject(message.toString());
        }catch(Exception e)
        {
            this.window.appendTextInfoArea(e.toString());
        }
        
        Thread ct = new Thread(new CommandWriter());
        ct.start();
        
        try{
            in = new ObjectInputStream(this.clientSocket.getInputStream());
            ControlMessage m = new ControlMessage();
            
            while(!this.window.isConnectionCanceled())
            {
                /*Receive message*/
                m.fromString((String)in.readObject());
                this.window.appendTextInfoArea(m.toReadableString());
                
                if(m.getCode() == Constants.END_COMMUNICATION_CODE)
                {
                     this.window.setStatusText("Conexão cancelada pelo controle");
                     this.window.setButtonText("Fechar servidor");
                     this.serverSocket.close();
                     cancel = true;
                     return;
                }
                
                switch(m.getAction())
                {
                    case Constants.MOTION_RIGHT:
                        commands[Constants.MOTION_RIGHT] = !commands[Constants.MOTION_RIGHT];
                    break;
                    case Constants.MOTION_LEFT:
                        commands[Constants.MOTION_LEFT] = !commands[Constants.MOTION_LEFT];                   
                    break;
                    case Constants.MOVEMENT_RIGHT:
                        commands[Constants.MOVEMENT_RIGHT] = !commands[Constants.MOVEMENT_RIGHT];
                    break;
                    case Constants.MOVEMENT_LEFT:
                        commands[Constants.MOVEMENT_LEFT] = !commands[Constants.MOVEMENT_LEFT];
                    break;
                    case Constants.MOVEMENT_DOWN:
                        commands[Constants.MOVEMENT_DOWN] = !commands[Constants.MOVEMENT_DOWN];
                    break;
                    case Constants.MOVEMENT_UP:
                        commands[Constants.MOVEMENT_UP] = !commands[Constants.MOVEMENT_UP];
                    break;
                    case Constants.BTN_RIGHT:
                        commands[Constants.BTN_RIGHT] = !commands[Constants.BTN_RIGHT];
                    break;
                    case Constants.BTN_LEFT:
                        commands[Constants.BTN_LEFT] = !commands[Constants.BTN_LEFT];
                    break;
                    case Constants.BTN_UP:
                        commands[Constants.BTN_UP] = !commands[Constants.BTN_UP];
                    break;
                    case Constants.BTN_DOWN:
                        commands[Constants.BTN_DOWN] = !commands[Constants.BTN_DOWN];
                    break;
                    case Constants.BTN_MENU:
                        commands[Constants.BTN_MENU] = !commands[Constants.BTN_MENU];
                    break;
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
    
    private class CommandWriter implements Runnable {
        @Override
        public void run()
        {
            try{
            Robot r = new Robot();
            r.setAutoDelay(1);
            
            while(!cancel)
            {
                for(int i=0;i<12;i++){
                    if(commands[i]){
                        r.keyPress(ControlConfiguration.getKey(i));
                        isPressed[i] = true;
                    }
                }
                
                for(int i=0;i<12;i++){
                    if(!commands[i] && isPressed[i]){
                        r.keyRelease(ControlConfiguration.getKey(i));
                        isPressed[i] = false;
                    }
                }  
            }
            }catch(Exception e){System.out.println(e.toString());e.printStackTrace();} 
        }
    }
    
}