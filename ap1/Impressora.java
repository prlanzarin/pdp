import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.List;

public class Impressora implements Runnable {

	int maxPressTime; // Java time
	int maxPress;
	int nofPrints;
;

	public Impressora(int mt, int x) {
		this.maxPressTime = mt;
		this.maxPress = x;
		this.nofPrints = 0;
	}

	public boolean printJobs() {
		// sleep nofJobs * 5s
		// return etc
		if(this.isAble()) {

                       try {
				this.nofPrints++;
				Thread.sleep(this.maxPressTime*100);
			} catch (InterruptedException e) {return false;}

			System.out.println("Printer is done." + " It can handle " +
					(this.maxPress - this.nofPrints) + " more jobs.");

			return true;
		}
		return false;
	}

	public boolean isAble() {
		if((this.maxPress - this.nofPrints) > 0)
			return true;
		return false;
	}

	@Override
		public void run() {
			while(this.isAble()) {
				try{
				Thread.sleep(1000);
				} catch(InterruptedException e) {return;}
			}
			System.out.println("PRINTER: max number of prints per day reached...\n");
		}

}
