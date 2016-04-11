public class Usuario implements Runnable{

    int id;
    int models;
    PrintServer printServer; 


    public Usuario(int id, PrintServer printServer) {
        this.id = id;
        this.printServer = printServer;
        this.models = 0;
	printServer.incrementInstances();
    }

    @Override // thread
        public void run() {

		try {
			Main.loading.acquire();

		} catch (InterruptedException e) {
			return ;
		}
            	this.printServer.jobRequest(this.id);
		Main.loading.release();	

            	System.out.println("User " + this.id + " sent a job request");

        }
}
