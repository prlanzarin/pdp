import java.util.*;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Thread> usuarios = new ArrayList(0);
        Impressora impressora = new Impressora(5, 6);
        PrintServer printServer = new PrintServer(10, impressora);
        Thread impThread = new Thread(impressora);
        Thread serverThread = new Thread(printServer);

        impThread.start();
        serverThread.start();

        for(int i = 0; i < 20; i++) {
            Usuario u = new Usuario(i, printServer);	
            Thread ut = new Thread(u);
            usuarios.add(ut);
            ut.start();
        } 	
    }

}
