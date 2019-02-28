/**
	MIS Project, QueueingService Queueing_Centre Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package centers;
import java.util.ArrayList;
import commons.*;
import generators.*;

public class Queueing_Centre {
	
	private Generator gen;
	private int queueType;
	private boolean busy = false;
	private ArrayList<Job> Queue = null;
	private RandomGen QGen = null;
 	
	public Queueing_Centre(Generator gen, int type) {
		
		this.gen = gen;
		this.queueType = type;
		this.Queue = new ArrayList<Job>();
		this.QGen = new RandomGen(CommonVariables.seedRandQueue);
	}
	
	public void pushQueue(Job job) {
		this.Queue.add(job);
	}
	
	public Job popQueue(int type) {
		
		Job j = null;
		
		if (!this.Queue.isEmpty()) {
			
			if(type == CommonVariables.FIFOQueue) { 							
				j = Queue.get(0);
				this.Queue.remove(0);
			}
			else if (type == CommonVariables.LIFOQueue) {						
				j = Queue.get(Queue.size() - 1);
				this.Queue.remove(Queue.size() - 1);
			}
			else if (type == CommonVariables.RANDQueue) {										
				QGen.setModule(Queue.size());
				int position = (int)QGen.getNextNumber()%Queue.size();
				j = Queue.get(position);
				this.Queue.remove(position);
			}
		}
		
		return j;
	}
	
	public boolean isEmpty() {
		
		if(this.Queue.size() == 0) {
			return true;
		}
		
		return false;
	}
	
	public int size() {
		return this.Queue.size();
	}
	
	public int getQueueType() {
		return this.queueType;
	}
	
	public boolean isBusy() {
		return busy;
	}
	
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	public double getServiceTime() {
		return gen.getNextNumber();
	}
	 
	public Generator getGenerator() {
		return this.gen;
	}
	
	public void resetQGen() {
		
		long gen = QGen.getNextSeed();
		this.QGen.resetGen(gen);
	}
	
	public ArrayList<Job> getQueue() {
		return this.Queue;
	}
	
	public void clearQueue() {
		this.Queue.clear();
	}
	
	public void setQueueType(int t) {
		this.queueType = t;
	}
	
	public Queueing_Centre clone() {
		
		Queueing_Centre centre = new Queueing_Centre(this.getGenerator(), this.getQueueType());
		
		for(int i = 0;i<this.Queue.size();i++) {
			centre.getQueue().add(this.Queue.get(i));
		}
		
		centre.setBusy(this.busy);
		
		return centre;
	}
	
	public Queueing_Centre copyCentre(Queueing_Centre qc) {
		
		this.Queue.clear();
		
		for(int i = 0;i<qc.getQueue().size();i++){
			this.Queue.add(qc.getQueue().get(i));
		}

		this.gen = qc.getGenerator();
		this.queueType = qc.getQueueType();
		this.busy = qc.isBusy();
		
		return this;
	}
}