/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OpportunisticSensingServer;

/**
 *
 * @author Claudio Gisch e Paulo Lanzarin
 */

public class Constants {
    
    /**
     * Pre-defined server port. All connections should happen on this port.
     */
    public static final int SERVER_PORT_NUMBER = 26789;
    
    public static final int INITIATE_COMMUNICATION_CODE = 1;
    public static final int END_COMMUNICATION_CODE = 0;
    public static final int ACTION_SENT_CODE = 2;
    
    public static final String CODE_STRINGS[] = {"Fim de comunicação", "Início de comunicação", "Ação enviada"};  
        
}
