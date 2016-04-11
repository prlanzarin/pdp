import java.util.ArrayList;
import java.util.List;

public class Impressora implements Runnable {

	int maxPressTime; // Java time
	int maxPress;
	int nofPrints;
    boolean busy;

	public Impressora(int mt, int x) {
		this.maxPressTime = mt;
		this.maxPress = x;
        this.nofPrints = 0;
        this.busy = false;
	}

	public int getMaxPressTime() {
		return this.maxPressTime;
	}

	public int getMaxPress() {
		return this.maxPress;
	}

    public boolean printJobs() {
        // sleep nofJobs * 5s
        // return etc
        if(isAble()) {
            synchronized(this) {
                System.out.println("Printer is printing a packet...\n");
                try {
                    this.nofPrints++;
                    busy = true;
                    Thread.sleep(this.maxPressTime*100);
                    busy = false;
                } catch (InterruptedException e) {return false;}
                System.out.println("Printer is done." + " It can handle " +
                        (this.maxPress - this.nofPrints) + " more jobs.");
            }
            return true;
        }

        return false;
    }

    public boolean isAble() {
        if((this.maxPress - this.nofPrints) > 0)
            return true;
        return false;
    }
    
    public boolean isBusy() {
        return this.busy;
    }

    @Override
    public void run() {
        while(isAble())
            ;
        System.out.println("PRINTER: max number of prints per day reached...\n");
    }

}
