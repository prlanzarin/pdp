import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class PrintServer implements Runnable {

    Impressora impressora; // objeto impressora que imprime pacotes
    private int packetSize;  // tamanho do pacote (10 eg)
    ArrayList<Integer> pendingRequests; // fila de jobs (FIFO)
    static int nOfInstances; // contador estatico de instancias 

    public PrintServer(int p, Impressora i) {
        this.packetSize = p;
        this.impressora = i;
        this.pendingRequests = new ArrayList(0);
        this.nOfInstances = 0;
    }

    /* Serve como contador estatico de instancias de usuario. Sincronizado
     * para nao haver race condition */
    public synchronized int incrementInstances() {
        return this.nOfInstances++;
    }

    /* Metodo pelo qual usuarios poderao fazer a requisiçao de um job */
    public boolean jobRequest(int userId)  {
        if(!this.impressora.isAble())
            return false;
        // adiciona job a fila 
        this.pendingRequests.add(userId);

        System.out.println("SERVER queued a request");

        return true;
    }

    /* Metodo de PrintServer que envia um pacote a Impressora para ser impresso */
    public void sendPacket() {

        System.out.println("PrintServer is sending a packet to the printer...\n");
        // adquire-se o semaforo responsavel pelo envio do pacote
        try {
            Main.sendingPacket.acquire();	
        } catch (InterruptedException e) {
            return;
        }
        // envio do pacote para impressao
        this.impressora.printJobs();
        System.out.println(pendingRequests);
        // libera-se o semaforo
        Main.sendingPacket.release();

        System.out.println("PrintServer is free again, requeuing jobs...\n");

    }

    @Override
    public void run() {
        System.out.println("PrintServer is ON.\n");

        while(this.impressora.isAble()) {
            // se houver packetSize jobs, prepara um pacote e envia para impressao
            if(this.pendingRequests.size() >= this.packetSize) {
                //lista temporaria de jobs que vao ser impressos (pacote)
                ArrayList<Integer> printingJobs = new ArrayList(10);

                // adquire semaforo para alteracao da fila de jobs
                try {
                    Main.loading.acquire();	
                } catch (InterruptedException e) {
                    return;
                }
                // constroi o pacote removendo da fila pendingRequests
                for(int i = 0; i < packetSize; i++)
                    printingJobs.add(this.pendingRequests.remove(0));
                // libera o semaforo de pendingRequests
                Main.loading.release();
                // envio do pacote
                this.sendPacket();                
                // devolve os jobs impressos a fila apos a impressao
                // ser concluida. Aquisiçao e liberaçao do semofor loading
                try {
                    Main.loading.acquire();	
                } catch (InterruptedException e) {
                    return;
                }                
                for(int i = 0; i < packetSize; i++) 
                    this.pendingRequests.add(printingJobs.remove(0));
                Main.loading.release();
            }
        }
    }

}
