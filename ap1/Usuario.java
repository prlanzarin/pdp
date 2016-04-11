public class Usuario implements Runnable{
    int id; // id do usuario
    PrintServer printServer; // servidor de impressao para envio de jobs 
    public Usuario(int id, PrintServer printServer) {
        this.id = id;
        this.printServer = printServer;
        this.models = 0;
        printServer.incrementInstances(); // contador de instancias estatico
    }

    @Override // thread
        public void run() {

            // envia um job para o PrintServer (um por usuario)
            // adquire o lock de carregamento na fila de jobs pois sabe que vai
            // modifica-la. Somente uma thread pode alterar pendingRequests por vez
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
