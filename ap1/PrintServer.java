import java.util.ArrayList;

public class PrintServer implements Runnable {

    Impressora impressora;
    int packetSize;  
    ArrayList<Integer> pendingRequests;

    public PrintServer(int p, Impressora i) {
        this.packetSize = p;
        this.impressora = i;
        this.pendingRequests = new ArrayList(0);
    }

    public synchronized boolean jobRequest(int userId) {
        if(!this.impressora.isAble())
            return false;

        this.pendingRequests.add(userId);
        System.out.println("Queue size: " + this.pendingRequests.size());

        return true;
    }

    public void sendPacket() {

        System.out.println("PrintServer is sending a packet to the printer...\n");
        this.impressora.printJobs(this);

        synchronized(this){
            try {
                this.wait();
            } catch (InterruptedException e) {
                return;
            }
        }

        System.out.println("PrintServer is free again, requeuing jobs...\n");

    }

    @Override
    public void run() {
        System.out.println("PrintServer is ON.\n");

        while(this.impressora.isAble()) {

            System.out.println("ola");
            if(this.pendingRequests.size() >= this.packetSize) {
                ArrayList<Integer> printingJobs = new ArrayList(10);

                for(int i = 0; i < packetSize; i++)
                    printingJobs.add(this.pendingRequests.remove(0));

                this.sendPacket();                

                for(int i = 0; i < packetSize; i++) 
                    this.pendingRequests.add(printingJobs.remove(0));
            }
        }
    }

}
