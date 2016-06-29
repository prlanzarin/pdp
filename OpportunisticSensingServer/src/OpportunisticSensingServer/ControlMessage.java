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
public class ControlMessage {
       
    private int action;
    private int code;
    
    public ControlMessage()
    {
        this.code = 0;
        this.action = 0;
    }   
    
    public void setCode(int code)
    {
        this.code = code;
    }
    
    public void setAction(int action)
    {
        this.action = action;
    }
    
    public int getCode()
    {
        return this.code;
    }
    
    public int getAction()
    {
        return this.action;
    }
    
    
    public String toReadableString()
    {
        String string = "Código: "+Constants.CODE_STRINGS[this.code]+" | Ação: "+Constants.ACTION_STRINGS[this.action];
        return string;
    }
    
    @Override
    public String toString()
    {
        String string = "# "+this.code+" "+this.action;
        return string;
    }
    
    public void fromString(String s)
    {
        String strings[] = s.split(" ");
        this.code = Integer.parseInt(strings[1]);
        this.action = Integer.parseInt(strings[2]);
    }
    /*
    public void fromString(String string)
    {
        String s[] = string.split(" ");
        this.code = Integer.parseInt(s[1]);
        this.ax = Float.parseFloat(s[2]);
        this.ay = Float.parseFloat(s[3]);
        this.az = Float.parseFloat(s[4]);
    }*/
}
