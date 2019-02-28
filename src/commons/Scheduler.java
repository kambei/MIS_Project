/**
	MIS Project, Scheduler Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;
import centers.*;
import generators.*;
import java.util.ArrayList;

public class Scheduler {

	private Calendar calendar;
	private Clock clock;
	private int currentArrayPosition;
	private UniformGen pout = null;
	private RandomGen routing = null;
	private ArrayList<TERM_Centre> terminals;
	private ArrayList<CPUClient_Centre> cpuClients;
	private IS_Centre LAN1;
	private Queueing_Centre GW1; 
	private IS_Centre WAN; 
	private Queueing_Centre GW2;
	private IS_Centre LAN2; 
	private ArrayList<Queueing_Centre> cpuServers;
	private ArrayList<Queueing_Centre> ms; 
	private ArrayList<Job> jobs;
	private ArrayList<Double> returns = null; 
	private int N;
	private int xWAN = 0; 
	
	public Scheduler(Calendar cal, Clock c, 
			ArrayList<TERM_Centre> terminals,
			ArrayList<CPUClient_Centre> cpuClients, 
			IS_Centre LAN1,
			Queueing_Centre GW1, 
			IS_Centre WAN, 
			Queueing_Centre GW2,
			IS_Centre LAN2, 
			ArrayList<Queueing_Centre> cpuServers,
			ArrayList<Queueing_Centre> ms, 
			ArrayList<Job> jobs, int N) {

		this.calendar = cal;
		this.clock = c;
		this.currentArrayPosition = 0;
		this.pout = new UniformGen(new RandomGen(CommonVariables.seedExit));
		this.routing = new RandomGen(CommonVariables.seedRouting);	
		this.terminals = terminals;
		this.cpuClients = cpuClients;
		this.LAN1 = LAN1;
		this.GW1 = GW1;
		this.WAN = WAN;
		this.GW2 = GW2;
		this.LAN2 = LAN2;
		this.cpuServers = cpuServers;
		this.ms = ms;
		this.jobs = jobs;
		this.returns = new ArrayList<Double>(); 
		this.N = N;
	}

	public Event nextEvent() {

		Event min = new Event(-1, Event.INFINITY);
		
		for (int i = 0; i < this.calendar.getCalendar().size(); i++) {
			if (min.getTime() > this.calendar.getCalendar().get(i).getTime()) {
				min = this.calendar.getCalendar().get(i);
				min.setID(calendar.getCalendar().get(i).getID());
				this.currentArrayPosition = i;
			}
		}
		
		return min;
	}

	public void simulateRun(int obs) {
		
		if(CommonVariables.printSched)System.out.println("(Scheduler) Simulate RUN with " + N + " HOST-CLIENTS and " + obs + " observations!!!\n");
		
		this.clock.startClock();
		int eventType = 0;
		int jobID = -1;
		int r = 0;
		int responce = 0;
		double delta = 0.0;
		Event nextEvent = null;

		while (responce < obs) {
			
			nextEvent = this.nextEvent();
			eventType = nextEvent.getID();
			clock.setSimulationTime(nextEvent.getTime());
			if(CommonVariables.printSchedJobs)System.out.println(nextEvent.getID());
			if(CommonVariables.printSchedCalendar)this.calendar.viewCalendar();

			if (nextEvent.getTime() != Event.INFINITY) {

				jobID = this.currentArrayPosition % N;

				switch (eventType) {

					case Event.END_TERM:		 // END_TERM Routine
						
						delta = cpuClients.get(jobID).getServiceTime();
						
						calendar.setCalendar(currentArrayPosition + N,
								new Event(Event.END_CPC, clock.getSimulationTime() + delta));

						calendar.setCalendar(currentArrayPosition, 
								new Event(Event.END_TERM, Event.INFINITY));
						
						jobs.get(jobID).resetJob();
					
						if(CommonVariables.printSchedJobs)System.out.println("END_TERM Job " + jobID + " - with TIME: " + jobs.get(jobID).getResponceTime() +" sec");
						
					break;

					case Event.END_CPC: 		// END_CPc Routine

						delta = LAN1.getServiceTime(jobs.get(jobID));
						
						jobs.get(jobID).setStartServiceTime(clock.getSimulationTime());
					
						calendar.setCalendar(currentArrayPosition + N,
								new Event(Event.END_LAN1, clock.getSimulationTime() + delta));
						
						calendar.setCalendar(currentArrayPosition, 
								new Event(Event.END_CPC, Event.INFINITY));
						
						jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
					
						if(CommonVariables.printSchedJobs)System.out.println("END_CPc Job " + jobID + " - with TIME: " + jobs.get(jobID).getResponceTime() +" sec");

					break;

					case Event.END_LAN1: 		// END_LAN1 Routine
						
						if (jobs.get(jobID).getJobClass() == 1) {
							
							jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
							delta = GW1.getServiceTime();
							
							if (GW1.isEmpty() && !GW1.isBusy()) {
								
								GW1.setBusy(true);								
							}
							
							calendar.setCalendar(5 * N,
									new Event(Event.END_GW1, clock.getSimulationTime() + delta));
							
							GW1.pushQueue(jobs.get(jobID));
							
							if(CommonVariables.printSchedJobs)System.out.println("END_LAN1 Job " + jobID + " - of Class:" + jobs.get(jobID).getJobClass()
									+ " - with TIME: " + jobs.get(jobID).getResponceTime()+" sec");						
						} else {

							delta = terminals.get(jobID).getServiceTime();
							jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
							
							responce++;
							returns.add(jobs.get(jobID).getResponceTimeREAL());
							jobs.get(jobID).resetJob();
							
							if(CommonVariables.printSchedJobs)System.out.println("\n=>END_LAN1 for JOB "+ jobs.get(jobID).getJobID()
								+ " - of Class" + jobs.get(jobID).getJobClass() + " - with TIME -----> R: " +  jobs.get(jobID).getResponceTime() +" sec"+"!!!!!!!!!!\n");
							
							calendar.setCalendar(this.currentArrayPosition - 2 * (N),
									new Event(Event.END_TERM, clock.getSimulationTime() + delta));
						}
						
						calendar.setCalendar(currentArrayPosition, 
								new Event(Event.END_LAN1, Event.INFINITY));
					break;

					case Event.END_WAN: 		// END_WAN Routine
						
						jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
						
						// Se (SimulationTime > Stabilization Time && SimulationTime < Stabilization Time + WidhtOfTheWindow )
						// osservazione del Throughput all'interno della finestra temporale.
						if((clock.getSimulationTime() > CommonVariables.timeStab)      //WidhtOfTheWindow = RTT * fatt_moltiplicativo
								&& clock.getSimulationTime() < CommonVariables.timeStab + (CommonVariables.RTT * CommonVariables.multiplicativeFactor)){							
							
							this.xWAN++;
						}
					
						if (jobs.get(jobID).getJobClass() == 1) {
							
							delta = GW2.getServiceTime();
							
							if (GW2.isEmpty() && !GW2.isBusy()) {
	
								GW2.setBusy(true);
							}
							
							calendar.setCalendar(5 * N + 1,
									new Event(Event.END_GW2, clock.getSimulationTime() + delta));
							
							GW2.pushQueue(jobs.get(jobID));

						} else {
							
							delta = GW1.getServiceTime();
							
							if (GW1.isEmpty() && !GW1.isBusy()) {
	
								GW1.setBusy(true);								
							}
							
							calendar.setCalendar(5 * N,
									new Event(Event.END_GW1, clock.getSimulationTime() + delta));
							
							GW1.pushQueue(jobs.get(jobID));
						}
						
						calendar.setCalendar(currentArrayPosition, 
								new Event(Event.END_WAN, Event.INFINITY));

						
						if(CommonVariables.printSchedJobs)System.out.println("END_WAN Job " + jobID + " - of Class: " + jobs.get(jobID).getJobClass() 
								+ " - with TIME: " + jobs.get(jobID).getResponceTime()+" sec");
					break;

					case Event.END_LAN2: 		// END_LAN2 Routine
						
						jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
						
						if (jobs.get(jobID).getJobClass() == 1) {

							r = (int) (routing.getNextNumber()) % CommonVariables.numberOfServer;

							delta = cpuServers.get(r).getServiceTime();
							
							if (cpuServers.get(r).isEmpty() && !cpuServers.get(r).isBusy()) {
							
								cpuServers.get(r).setBusy(true);
							}
							
							calendar.setCalendar((5 * N) + 2 + r,
									new Event(Event.END_SPC, clock.getSimulationTime() + delta));
							
							cpuServers.get(r).pushQueue(jobs.get(jobID));

						} else {
							
							delta = GW2.getServiceTime();
							
							if (GW2.isEmpty() && !GW2.isBusy()) {
	
								GW2.setBusy(true);								
							}
							
							calendar.setCalendar(5 * N + 1,
									new Event(Event.END_GW2, clock.getSimulationTime() + delta));
							
							GW2.pushQueue(jobs.get(jobID));

						}
						
						calendar.setCalendar(currentArrayPosition, new Event(Event.END_LAN2, Event.INFINITY));

						if(CommonVariables.printSchedJobs)System.out.println("END_LAN2 Job " + jobID+ " - of Class: " + jobs.get(jobID).getJobClass()
								+ " - with TIME: " + jobs.get(jobID).getResponceTime()+" sec");
						
					break;

					case Event.END_GW1: 		// END_GW1 Routine
					
						if(CommonVariables.printSchedQueue){
							System.out.println("\n*************Queue GW1 size: " + GW1.size() + " Elements: ");
							System.out.print("TOP");
							for(int i=0; i<GW1.getQueue().size(); i++) {
								System.out.print(" <- " + GW1.getQueue().get(i).getJobID());
							}
							System.out.println("\n----EVENT:");
						}
						
							
						Job j = GW1.popQueue(GW1.getQueueType());
						jobID = j.getJobID();

						jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
						
						if (j.getJobClass() == 1) {
					
							delta = WAN.getServiceTime(jobs.get(jobID));
							calendar.setCalendar((3 * N) + jobID,
									new Event(Event.END_WAN, clock.getSimulationTime() + delta));

						} else {
							
							LAN1.getCenters().get(jobID).setAverage(0.00008);
							
							delta = LAN1.getServiceTime(jobs.get(jobID));
							calendar.setCalendar((2 * N) + jobID,
									new Event(Event.END_LAN1, clock.getSimulationTime() + delta));

							LAN1.getCenters().get(jobID).setAverage(0.00016);
						}

						if (GW1.isEmpty()) {

							calendar.setCalendar(currentArrayPosition, 
									new Event(Event.END_GW1, Event.INFINITY));

						} else {

							delta = GW1.getServiceTime();
							calendar.setCalendar(currentArrayPosition,
									new Event(Event.END_GW1, clock.getSimulationTime() + delta));
						}
						
						GW1.setBusy(false);
	
						if(CommonVariables.printSchedJobs)System.out.println("END_GW1 Job " + jobID + " - of Class: " + jobs.get(jobID).getJobClass() 
								+ " - with TIME: " + jobs.get(jobID).getResponceTime()+" sec");
						
					break;

					case Event.END_GW2: 		// END_GW2 Routine
					
						if(CommonVariables.printSchedQueue){
							System.out.println("\n*************Queue GW2 size: " + GW2.size() + " Elements: ");
							System.out.print("TOP");
							for(int i=0; i<GW2.getQueue().size(); i++) {
								System.out.print(" <- " + GW2.getQueue().get(i).getJobID());
							}
							System.out.println("\n----EVENT:");
						}

						Job k = GW2.popQueue(GW2.getQueueType());
						jobID = k.getJobID();
						
						jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
						
						if (k.getJobClass() == 1) {
							
							delta = LAN2.getServiceTime(jobs.get(jobID));
							calendar.setCalendar((4 * N) + jobID,
									new Event(Event.END_LAN2, clock.getSimulationTime() + delta));
						} else {
							
							delta = WAN.getServiceTime(jobs.get(jobID));
							calendar.setCalendar((3 * N) + jobID,
									new Event(Event.END_WAN, clock.getSimulationTime() + delta));
						}

						if (GW2.isEmpty()) {
							
							calendar.setCalendar(currentArrayPosition, new Event(
								Event.END_GW2, Event.INFINITY));
					
						} else {
						
							delta = GW2.getServiceTime();
							calendar.setCalendar(currentArrayPosition,
									new Event(Event.END_GW2, clock.getSimulationTime() + delta));
						}

						GW2.setBusy(false);
						if(CommonVariables.printSchedJobs)System.out.println("END_GW2 Job " + jobID + " - of Class: " + jobs.get(jobID).getJobClass()  
								+ " - with TIME: " + jobs.get(jobID).getResponceTime()+" sec");
					
					break;

				    case Event.END_SPC: 		// END_SPc Routine
				    	
				    	if(CommonVariables.printSchedQueue){
				    		System.out.println("\n************* Queue SPc#" + (this.currentArrayPosition - (5 * N) - 2) 
					    			+ " size: " + (cpuServers.get(this.currentArrayPosition - (5 * N) - 2).size()) + " Elements: ");
				    		System.out.print("TOP");
							for(int i=0; i<cpuServers.get(this.currentArrayPosition - (5 * N) - 2).getQueue().size(); i++) {
								System.out.print(" <- " + cpuServers.get(this.currentArrayPosition - (5 * N) - 2).getQueue().get(i).getJobID());
							}
							System.out.println("\n----EVENT:");
						}
				    	
				    	Job i = cpuServers.get(this.currentArrayPosition - (5 * N) - 2).popQueue(0);
				    	jobID = i.getJobID();
				    	
				    	jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
				    	
				    	double p = pout.getNextNumber();
					
				    	if (p <= CommonVariables.exitProbability) {
				    		
				    		LAN2.getCenters().get(jobID).setAverage(0.00016);
				    		
				    		delta = LAN2.getServiceTime(jobs.get(jobID));
				    		jobs.get(jobID).setJobClass(2);
				    		
				    		calendar.setCalendar((4 * N) + jobID,
				    				new Event(Event.END_LAN2, clock.getSimulationTime() + delta));
				    		
				    		LAN2.getCenters().get(jobID).setAverage(0.00032);

				    	} else {
				    		
				    		delta = ms.get(this.currentArrayPosition + 3 - 5 * N - 5).getServiceTime();
				    		
				    		if (ms.get(this.currentArrayPosition + 3 - 5 * N - 5).isEmpty()
								&& !ms.get(this.currentArrayPosition + 3 - 5 * N - 5).isBusy()) {
	
				    			ms.get(this.currentArrayPosition + 3 - 5 * N- 5).setBusy(true);				    			
				    		}
				    		
				    		calendar.setCalendar(this.currentArrayPosition + 3,
			    					new Event(Event.END_MS,clock.getSimulationTime() + delta));
				    		
				    		ms.get(this.currentArrayPosition + 3 - 5 * N - 5).pushQueue(jobs.get(jobID));
				    	}

				    	if (cpuServers.get(this.currentArrayPosition - (5 * N) - 2).isEmpty()) {

				    		calendar.setCalendar(currentArrayPosition, 
				    				new Event(Event.END_SPC, Event.INFINITY));

				    	} else {

				    		delta = cpuServers.get(this.currentArrayPosition - (5 * N) - 2).getServiceTime();
				    		calendar.setCalendar(currentArrayPosition,
				    				new Event(Event.END_SPC,clock.getSimulationTime() + delta));
				    	}

				    	cpuServers.get(this.currentArrayPosition - (5 * N) - 2).setBusy(false);
				    	
				    	if(CommonVariables.printSchedJobs)System.out.println("END_SPc Job " + jobID + " - of Class: " + jobs.get(jobID).getJobClass() 
								+ " - with TIME: " + jobs.get(jobID).getResponceTime()+" sec");
				    
				    break;

					case Event.END_MS: 			// END_Ms Routine
						
						if(CommonVariables.printSchedQueue){
				    		System.out.println("\n************* Queue SMs#" + (this.currentArrayPosition - (5 * N) - 5) 
					    			+ " size: " + (ms.get(this.currentArrayPosition - (5 * N) - 5).size()) + " Elements: ");
				    		System.out.print("TOP");
				    		
							for(int kr=0; kr<ms.get(this.currentArrayPosition - (5 * N) - 5).getQueue().size(); kr++) {
								System.out.print(" <- " + ms.get(this.currentArrayPosition - (5 * N) - 5).getQueue().get(kr).getJobID());
							}
							System.out.println("\n----EVENT:");
						}

						Job h = ms.get(this.currentArrayPosition - (5 * N) - 5).popQueue(2);
						jobID = h.getJobID();
						
						jobs.get(jobID).setEndServiceTime(clock.getSimulationTime());
						delta = cpuServers.get(this.currentArrayPosition - 3 - (5 * N) - 2).getServiceTime();
						
						if (cpuServers.get(this.currentArrayPosition - 3 - (5 * N) - 2).isEmpty()
							&& !cpuServers.get(this.currentArrayPosition - 3 - (5 * N) - 2).isBusy()) {
							
							cpuServers.get(this.currentArrayPosition - 3 - (5 * N) - 2).setBusy(true);
						}
						
						calendar.setCalendar(this.currentArrayPosition - 3,
								new Event(Event.END_SPC,(clock.getSimulationTime()) + delta));
						
						cpuServers.get(this.currentArrayPosition - 3 - (5 * N) - 2).pushQueue(jobs.get(jobID));

						if (ms.get(this.currentArrayPosition - (5 * N) - 5).isEmpty()) {

							calendar.setCalendar(currentArrayPosition, 
									new Event(Event.END_MS, Event.INFINITY));

						} else {

							delta = ms.get(this.currentArrayPosition - (5 * N) - 5).getServiceTime();
							calendar.setCalendar(currentArrayPosition,
									new Event(Event.END_MS,clock.getSimulationTime() + delta));
						}
						
						ms.get(this.currentArrayPosition - (5 * N) - 5).setBusy(false);
						if(CommonVariables.printSchedJobs)System.out.println("END_Ms Job " + jobID + " - of Class: " + jobs.get(jobID).getJobClass() 
								+ " - with TIME: " + jobs.get(jobID).getResponceTime()+" sec");
					break;
				}
			}
		}
		
		if(CommonVariables.printSched)System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> Number of Returns:" + responce);
		if(CommonVariables.printSched)System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> Simulation Time: " + clock.getSimulationTime()+" sec");
		if(CommonVariables.printSched)System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> REAL Time: " + clock.getRealTime() +" msec\n");
	}

	public ArrayList<TERM_Centre> getTerminals() {
		return terminals;
	}

	public ArrayList<Double> getReturns() {
		return returns;
	}
	
	public void resetReturns() {
		this.returns.clear();
	}
	
	public void startClock(){
		this.clock.startClock();
	}
	
	public double stopClock(){
		return this.clock.getRealTime();
	}
	
	public void resetRoutingGen() { 
		
		long p = pout.getNextSeed();
		this.pout.resetGen(p);
		
		long rout = routing.getNextSeed();
		this.routing.resetGen(rout);
	}
	
	public int getXWAN(){
		return this.xWAN;
	}
	
	public void resetXWAN() {
		this.xWAN = 0;
	}
	
	public void setClock(Clock t) {
		this.clock.setSimulationTime(t.getSimulationTime());
	}
	
	public void setCalendar(Calendar c) {
		this.calendar = c;
	}
	
	public void setTerminals(ArrayList<TERM_Centre> terminals) {
		this.terminals = terminals;
	}

	public void setCpuClients(ArrayList<CPUClient_Centre> cpuClients) {
		this.cpuClients = cpuClients;
	}

	public void setLAN1(IS_Centre lAN1) {
		LAN1 = lAN1;
	}

	public void setGW1(Queueing_Centre gW1) {
		this.GW1 = gW1;
	}

	public void setWAN(IS_Centre wAN) {
		WAN = wAN;
	}

	public void setGW2(Queueing_Centre gW2) {
		GW2 = gW2;
	}

	public void setLAN2(IS_Centre lAN2) {
		LAN2 = lAN2;
	}

	public void setCpuServers(ArrayList<Queueing_Centre> cpuServers) {
		this.cpuServers = cpuServers;
	}

	public void setMs(ArrayList<Queueing_Centre> ms) {
		this.ms = ms;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public Clock getClock() {
		return clock;
	}

	public ArrayList<CPUClient_Centre> getCpuClients() {
		return cpuClients;
	}

	public IS_Centre getLAN1() {
		return LAN1;
	}

	public Queueing_Centre getGW1() {
		return GW1;
	}

	public IS_Centre getWAN() {
		return WAN;
	}

	public Queueing_Centre getGW2() {
		return GW2;
	}

	public IS_Centre getLAN2() {
		return LAN2;
	}

	public ArrayList<Queueing_Centre> getCpuServers() {
		return cpuServers;
	}

	public ArrayList<Queueing_Centre> getMs() {
		return ms;
	}

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setxWAN(int xWAN) {
		this.xWAN = xWAN;
	}
}