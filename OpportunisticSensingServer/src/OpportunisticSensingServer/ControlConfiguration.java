/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OpportunisticSensingServer;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.HashMap;
/**
 *
 * @author Usuário
 */
public class ControlConfiguration {
    private static Map<Integer, Integer> keyMap = new HashMap<Integer, Integer>();
    
    public static void setDefaultConfiguration()
    {
        keyMap.put(Constants.MOVEMENT_DOWN, KeyEvent.VK_DOWN);
        keyMap.put(Constants.MOVEMENT_UP, KeyEvent.VK_UP);
        keyMap.put(Constants.MOVEMENT_RIGHT, KeyEvent.VK_RIGHT);
        keyMap.put(Constants.MOVEMENT_LEFT, KeyEvent.VK_LEFT);
        keyMap.put(Constants.BTN_DOWN, KeyEvent.VK_UP);
        keyMap.put(Constants.BTN_UP, KeyEvent.VK_Y);
        keyMap.put(Constants.BTN_RIGHT, KeyEvent.VK_B);
        keyMap.put(Constants.BTN_LEFT, KeyEvent.VK_X);
        keyMap.put(Constants.BTN_MENU, KeyEvent.VK_ESCAPE);
        keyMap.put(Constants.MOTION_RIGHT, KeyEvent.VK_D);
        keyMap.put(Constants.MOTION_LEFT, KeyEvent.VK_A);
    }
    
    public static int getKey(int command)
    {
        return keyMap.get(command);
    }
    
}
