package OpportunisticSensingServer;

import java.net.InetAddress;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Claudio Gisch e Paulo Lanzarin
 */
public class ConnectionThread extends Thread{
    
    private ServerThread  st = null;
    private javax.swing.JTextPane informationArea = null;
    private MainWindow1 m = null;
    
    /**
     * Creates a new ConnectionThread objetc.
     * @param informationArea 
     */
    ConnectionThread(MainWindow1 m)
    {
        this.informationArea = m.getInformationArea();
        this.m = m;
    }
    

    /**
     * Creates the server Thread and waits for client connections.
     */   
    @Override
    public void run()
    {
        try{
            st = new ServerThread(Constants.SERVER_PORT_NUMBER);
            this.informationArea.setText(this.informationArea.getText()+"\nServidor inicializado...\nServidor conectado a porta "+st.getServerPort());
            this.informationArea.setText(this.informationArea.getText()+"\nAguardo conexão do cliente Android...");
            this.informationArea.setText(this.informationArea.getText()+"\nEndereço IP e Porta para conexão direta:  "+InetAddress.getLocalHost().getHostAddress()+":"+Constants.SERVER_PORT_NUMBER);
            JOptionPane.showMessageDialog(null, "Informe o seguinte IP e Porta no aplicativo:\n\tIP: "+InetAddress.getLocalHost().getHostAddress() + "\nPorta: "+Constants.SERVER_PORT_NUMBER, 
                "IP do Servidor", JOptionPane.PLAIN_MESSAGE);
            st.acceptClient();
            this.informationArea.setText(this.informationArea.getText()+"\nConexão com Android estabelecida ...\nAndoird conectado em "+st.getClientAddress());
        }catch(Exception e)
        {
            this.informationArea.setText(this.informationArea.getText()+"\n"+e.toString());
        }
        
        this.m.killWindow();
    }
    
    public ServerThread getServerThread()
    {
        return this.st;
    }
    
    public boolean isConnected()
    {
        if(st == null)
           return false;
        
        return true;
    }
}
