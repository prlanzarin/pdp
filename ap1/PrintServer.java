import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class PrintServer implements Runnable {

	Impressora impressora;
	private int packetSize;  
	ArrayList<Integer> pendingRequests;
	private static int nOfInstances;
	
	public PrintServer(int p, Impressora i) {
		this.packetSize = p;
		this.impressora = i;
		this.pendingRequests = new ArrayList(0);
		this.nOfInstances = 0;
	}

	public synchronized int incrementInstances() {
		return this.nOfInstances++;
	}

	public boolean jobRequest(int userId)  {
		if(!this.impressora.isAble())
			return false;

		this.pendingRequests.add(userId);

		System.out.println("SERVER queued a request");

		return true;
	}

	public void sendPacket() {

		System.out.println("PrintServer is sending a packet to the printer...\n");
		System.out.println(pendingRequests);
		try {
			Main.sendingPacket.acquire();	
		} catch (InterruptedException e) {
			return;
		}
		this.impressora.printJobs();
		Main.sendingPacket.release();

		System.out.println("PrintServer is free again, requeuing jobs...\n");

	}

	@Override
		public void run() {
			System.out.println("PrintServer is ON.\n");

			while(this.impressora.isAble()) {
				if(this.pendingRequests.size() >= this.packetSize) {
					ArrayList<Integer> printingJobs = new ArrayList(10);

					try {
						Main.loading.acquire();	
					} catch (InterruptedException e) {
						return;
					}

					for(int i = 0; i < packetSize; i++)
						printingJobs.add(this.pendingRequests.remove(0));
					Main.loading.release();

					this.sendPacket();                

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
