/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OpportunisticSensingServer;

/**
 *
 * @author Usuário
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
      
    public static final int MOTION_RIGHT = 1;
    public static final int MOTION_LEFT = 2;
    
    public static final int MOVEMENT_RIGHT = 3;
    public static final int MOVEMENT_LEFT = 4;
    public static final int MOVEMENT_UP = 5;
    public static final int MOVEMENT_DOWN = 6;
    
    public static final int BTN_RIGHT = 7;
    public static final int BTN_LEFT = 8;
    public static final int BTN_UP = 9;
    public static final int BTN_DOWN = 10;
    static final int BTN_MENU = 11;
        
    public static final String ACTION_STRINGS[] = {"-", "Movimento Direita", "Movimento Esquerda", "Seta Direita",
                                                    "Seta Esquerda", "Seta Cima", "Seta Baixo", 
                                                    "Botão Direito", "Botão Esquerdo", "Botão Cima", "Botão Baixo",
                                                    "Menu"
                                                    };
    
        
}
