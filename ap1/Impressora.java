import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.List;

public class Impressora implements Runnable {

    int maxPressTime; // tempo de impressao 
    int maxPress; // numero maximo de prints por dia
    int nofPrints; // numero de pacotes impressos ate o momento

    public Impressora(int mt, int x) {
        this.maxPressTime = mt;
        this.maxPress = x;
        this.nofPrints = 0;
    }

    /* Imprime um pacote se a impressora nao atingiu seu limite (isAble() == TRUE) */
    public boolean printJobs() {
        if(this.isAble()) {
            try {
                this.nofPrints++; // houve uma nova impressao
                Thread.sleep(this.maxPressTime*100); // esta imprimindo
            } catch (InterruptedException e) {return false;}

            System.out.println("Printer is done." + " It can handle " +
                    (this.maxPress - this.nofPrints) + " more jobs.");
            return true;
        }
        return false;
    }

    /* Retorna TRUE se nao atingiu o maximo de impressoes diarias, do contrario FALSE */
    public boolean isAble() {
        if((this.maxPress - this.nofPrints) > 0)
            return true;
        return false;
    }

    @Override
    /* A thread da impressora fica executando para receber chamadas de printJobs()
     * enquanto nao estiver no maximo */
    public void run() {
        while(this.isAble()) {
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e) {return;}
        }
        System.out.println("PRINTER: max number of prints per day reached...\n");
    }

}
