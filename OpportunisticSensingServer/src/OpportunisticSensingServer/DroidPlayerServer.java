/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OpportunisticSensingServer;

import java.awt.event.KeyEvent;
import java.awt.Robot;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Claudio Gisch e Paulo Lanzarin
 */
public class DroidPlayerServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){}
        
        MainWindow1 m = new MainWindow1();

        m.setVisible(true);
               
        while(!m.isWindowKilled())
        {
            try{
                /*Verify each second*/
                Thread.sleep(1000);
            }catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }

        ServerThread st = m.getServerThread();
        /*ConfigurationDialog d = new ConfigurationDialog(m, true);*/
              
        m.setVisible(false);
        
        /*if(d.wantToConfigCommands())
            System.out.println("I WANT TO CONFIGURE!!!\n");
        else
            System.out.println("Nah, I'm ok.\n");*/
        
        m.dispose();
        
        MainWindow2 m2 = new MainWindow2(st);
        
        if(!m2.startServerThread())
        {
            System.exit(0);
        }
        
        m2.setVisible(true); 
          
        while(!m2.isConnectionCanceled()){
             try{
                /*Verify each second*/
                Thread.sleep(1000);
            }catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
        
        System.exit(0);
    }
    
}
