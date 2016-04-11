import java.util.*;
import java.util.concurrent.Semaphore;

public class Main {
    // semaforo que controla a manipulaçao da lista de jobs
    public static Semaphore loading = new Semaphore(1, true);
    // semaforo que controla o envio e impressao dos pacotes
    public static Semaphore sendingPacket = new Semaphore(1, true);
    // semaforo que controla o processo de impressao
    public static Semaphore printing = new Semaphore(1, true);

    public static void main(String[] args) {
        ArrayList<Thread> usuarios = new ArrayList(0);
        Impressora impressora = new Impressora(5, 6);
        PrintServer printServer = new PrintServer(10, impressora);
        Thread impThread = new Thread(impressora);
        Thread serverThread = new Thread(printServer);

        // criamos as 20 threads de usuarios e as iniciamos
        for(int i = 0; i < 20; i++) {
            Usuario u = new Usuario(i, printServer);	
            Thread ut = new Thread(u);
            usuarios.add(ut);
            ut.start();
        } 	
        // inicia as threads do PrintServer e da Impressora
        serverThread.start();
        impThread.start();
    }
}
