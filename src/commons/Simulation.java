/**
	MIS Project, Simulation Class ( Main Class )
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;
import generators.*;
import gui.UI;
import centers.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

public class Simulation {

	private UI gui = null;
	private TestGenerator test = null;
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
	private Scheduler scheduler;
	private Calendar calendar;
	private Clock timer;
	private ArrayList<Integer> xWANs;
	
	private double averageMin = 0.0;
	private double averageMax = 0.0;
	
	public Simulation() throws IOException {

		this.setupNet(CommonVariables.numberOfClient);
		this.gui = new UI(this);
		this.test = new TestGenerator();
		this.gui.initUI();	
	}

	public Simulation(int N) {
		this.setupNet(N);
	}
	
	public static void main(String[] args) throws IOException {

		new Simulation();
	}

	public ArrayList<JFreeChart> testG() {

		ArrayList<JFreeChart> list = new ArrayList<JFreeChart>();

		RandomGen G = new RandomGen(301);
		UniformGen uG = new UniformGen(G);
		ExpGen eG = new ExpGen(uG, 0.6);
		HyperExpGen exG = new HyperExpGen(5033, 0.3, 0.8);
		KerlGen kG1 = new KerlGen(621, 0.2, 1);
		KerlGen kG2 = new KerlGen(621, 0.3, 5);
		KerlGen kG3 = new KerlGen(621, 0.4, 20);
		
		list.add(this.test.Test(G));
		list.add(this.test.Test(uG));
		list.add(this.test.Test(eG));
		list.add(this.test.Test(exG));
		list.add(this.test.Test(kG1));
		list.add(this.test.Test(kG2));
		list.add(this.test.Test(kG3));

		return list;
	}

	public void setupNet(int N) {

		calendar = new Calendar(N);
		timer = new Clock();
		jobs = new ArrayList<Job>();

		int seedTerm = CommonVariables.seedTerm;
		int seedCPUClient = CommonVariables.seedCPUClient;
		int seedLAN1 = CommonVariables.seedLAN1;
		int seedWAN = CommonVariables.seedWAN;
		int seedLAN2 = CommonVariables.seedLAN2;
		int seedGW1 = CommonVariables.seedGW1;
		int seedGW2 = CommonVariables.seedGW2;
		int seedSPC = CommonVariables.seedSPC;
		int seedMs = CommonVariables.seedMs;
		
		terminals = new ArrayList<TERM_Centre>();
		cpuClients = new ArrayList<CPUClient_Centre>();
		
		ArrayList<Generator> genLANs1 = new ArrayList<Generator>();
		ArrayList<Generator> genWANs = new ArrayList<Generator>();
		ArrayList<Generator> genLANs2 = new ArrayList<Generator>();
		
		LAN1 = new IS_Centre(genLANs1);
		WAN = new IS_Centre(genWANs);
		LAN2 = new IS_Centre(genLANs2);

		GW1 = new Queueing_Centre(new KerlGen(seedGW1, 0.0005, 4), CommonVariables.LIFOQueue);
		GW2 = new Queueing_Centre(new HyperExpGen(seedGW2, 0.6, 0.0005), CommonVariables.LIFOQueue);
		
		cpuServers = new ArrayList<Queueing_Centre>();
		ms = new ArrayList<Queueing_Centre>();

		for(int i = 0; i < N; i++){

			jobs.add(new Job(i, 0, 0, 0));
			terminals.add(new TERM_Centre(i, new ExpGen(new UniformGen(new RandomGen(seedTerm)), 25)));
			cpuClients.add(new CPUClient_Centre(i, new ExpGen(new UniformGen(new RandomGen(seedCPUClient)), 7)));
			genLANs1.add(new KerlGen(seedLAN1, 0.00016, 3));
			genWANs.add(new ExpGen(new UniformGen(new RandomGen(seedWAN)),0.003));
			genLANs2.add(new HyperExpGen(seedLAN2, 0.4, 0.00032));
			
			seedTerm = seedTerm + 2;
			if(seedTerm % 5 == 0){ seedTerm +=2 ; }
			seedCPUClient = seedCPUClient + 2;
			if(seedCPUClient % 5 == 0){ seedCPUClient +=2 ; }
			seedLAN1 = seedLAN1 + 2;
			if(seedLAN1% 5 == 0){ seedLAN1 +=2 ; }
			seedWAN = seedWAN + 2;
			if(seedWAN % 5 == 0){ seedWAN +=2 ; }
			seedLAN2 = seedLAN2 + 2;
			if(seedLAN2 % 5 == 0){ seedLAN2 +=2 ; }
		}
		
		for(int i = 0; i < CommonVariables.numberOfServer; i++){

			cpuServers.add(new Queueing_Centre(new HyperExpGen(seedSPC, 0.3, 0.00284), CommonVariables.FIFOQueue));
			ms.add(new Queueing_Centre(new KerlGen(seedMs, 0.0333333, 3), CommonVariables.RANDQueue));

			seedSPC = seedSPC + 2;
			if(seedSPC % 5 == 0){ seedSPC +=2 ; }
			seedMs = seedMs + 2;
			if(seedMs % 5 == 0){ seedMs +=2 ; }
		}
		
		this.xWANs = new ArrayList<Integer>();
		initCalendar(terminals, N);
		
		scheduler = new Scheduler(calendar, timer, terminals, cpuClients, LAN1, GW1, WAN, GW2, LAN2, cpuServers, ms, jobs, N);	
	}
	
	public void startStabilization(int N) {
		
		if(CommonVariables.printSim)System.out.println("---------------------STARTING STABILIZATION RUNs for " + CommonVariables.numberOfClient + " CLIENTs");
		
		CommonVariables.userStab = true;
		Stabilizator stabilizator = new Stabilizator(scheduler,this);
		stabilizator.stabilize(N);

		CommonVariables.stabObsUser = CommonVariables.stabilizationObs-1; 
		if(CommonVariables.printSim)System.out.println("\n+++++++++++++++Number of Observation for Observation RUNs setted at value: " 
									+ CommonVariables.stabObsUser+" !!!");
	}
	
	public void startObservation(int N) {
		
		FileWriter fw1 = null;
		FileWriter fw2 = null;
		
		try {
			fw1 = new FileWriter("ConfidenceIntervals.ods");  
			fw2 = new FileWriter("ThroughputValues.ods");	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        PrintWriter pw1 = new PrintWriter(fw1);
        PrintWriter pw2 = new PrintWriter(fw2);
        
        pw1.println("AVG_INF" + " " + "AVG_SUP");
		pw1.flush();
			
        for(int i = 1; i <= N; i++) {
			
        	if(CommonVariables.printSim)System.out.println("------------------------------------NUMBER OF CLIENT: " +i);

			Simulation tempSim = new Simulation(i);
			
			if(CommonVariables.userStab) {
				Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObsUser);
				obs.observ(i);
				
			} else {
			
				if(CommonVariables.numberOfClient == 12){
					Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObs12);
					obs.observ(i);
				} else if(CommonVariables.numberOfClient == 24) {
					Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObs24);
					obs.observ(i);
				} else if(CommonVariables.numberOfClient == 30) {
					Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObs30);
					obs.observ(i);
				} else if(CommonVariables.numberOfClient == 40) {
					Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObs40);
					obs.observ(i);
				} else if(CommonVariables.numberOfClient == 60) {
					Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObs60);
					obs.observ(i);
				} else if(CommonVariables.numberOfClient == 80) {
					Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObs80);
					obs.observ(i);
				} else if(CommonVariables.numberOfClient == 100) {
					Observer obs = new Observer(tempSim.getScheduler(),tempSim,CommonVariables.stabObs100);
					obs.observ(i);
				}	
			}
			
			pw1.println(tempSim.getAverageMin() + " " + tempSim.getAverageMax());
			pw1.flush();
			
			if(CommonVariables.printThroughput && tempSim.getXWANs().size() > 1 && CommonVariables.numberOfClient == CommonVariables.throughputObs)
				System.out.println("\n\n-------------THROUGHPUT OBSERVATED for " + tempSim.getXWANs().size() + " RUN!");
			
			if(i == CommonVariables.throughputObs) {

				for(int j = 0; j < tempSim.getXWANs().size(); j++){
					
					if(CommonVariables.printThroughput && CommonVariables.numberOfClient == CommonVariables.throughputObs)System.out.println("for RUN " + (j+1) + " is: " + tempSim.getXWANs().get(j) + " JOBs");
		
					pw2.println(tempSim.getXWANs().get(j));
					pw2.flush();
				
				}
				
				if(CommonVariables.graphThroughput && CommonVariables.numberOfClient == CommonVariables.throughputObs)getThroughputGraph(tempSim);
				
			}
			if(CommonVariables.printThroughput)System.out.println("----------------------------------------------------------------------");
		}

        pw1.close();
        pw2.close();

        try {
			fw1.close();
			fw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void resetGen(int N) {
		
		for(int i =	N; i<N; i++){
			
			long t = terminals.get(i).getGenerator().getNextSeed();
			terminals.get(i).getGenerator().resetGen(t);
			
			long c1 = cpuClients.get(i).getGenerator().getNextSeed();
			cpuClients.get(i).getGenerator().resetGen(c1);
			
			long l1 = LAN1.getCenters().get(i).getNextSeed();
			LAN1.getCenters().get(i).resetGen(l1);
			
			long w = WAN.getCenters().get(i).getNextSeed();
			WAN.getCenters().get(i).resetGen(w);
			
			long l2 = LAN2.getCenters().get(i).getNextSeed();
			LAN2.getCenters().get(i).resetGen(l2);
		}
			
		long gw1 = GW1.getGenerator().getNextSeed();
		GW1.getGenerator().resetGen(gw1);
		
		long gw2 = GW2.getGenerator().getNextSeed();
		GW2.getGenerator().resetGen(gw2);
		
		GW1.resetQGen();
		GW2.resetQGen();
		
		for (int i = 0; i<CommonVariables.numberOfServer; i++) {
			
			long c2 = cpuServers.get(i).getGenerator().getNextSeed();
			cpuServers.get(i).getGenerator().resetGen(c2);
			
			long m = ms.get(i).getGenerator().getNextSeed();
			ms.get(i).getGenerator().resetGen(m);
			
			cpuServers.get(i).resetQGen();
			ms.get(i).resetQGen();
		}
		
		this.scheduler.resetRoutingGen();
	}
	
	public void resetNet(int N) {
		
		for(int i=0; i<jobs.size(); i++){
			jobs.get(i).resetJob();
		}
		
		GW1.clearQueue();
		GW2.clearQueue();
		GW1.setBusy(false);
		GW2.setBusy(false);
		
		for(int i=0; i<CommonVariables.numberOfServer; i++){
			
			cpuServers.get(i).clearQueue();
			ms.get(i).clearQueue();
			cpuServers.get(i).setBusy(false);
			ms.get(i).setBusy(false);
		}
		
		for(int i=0; i<calendar.getSize(); i++){
			
			Event e = calendar.getEvent(i);
			calendar.setCalendar(i, new Event(e.getID(), Event.INFINITY));
		}
		
		initCalendar(terminals, N);
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public void initCalendar(ArrayList<TERM_Centre> term, int N) {

		for(int i = 0; i < N; i++){
			calendar.setCalendar(i, new Event(Event.END_TERM, term.get(i).getServiceTime()));
		}
	}

	public ArrayList<TERM_Centre> getTerminals() {
		return terminals;
	}

	public ArrayList<CPUClient_Centre> getCpuClients() {
		return cpuClients;
	}

	public IS_Centre getLAN1() {
		return LAN1;
	}

	public IS_Centre getWAN() {
		return WAN;
	}

	public IS_Centre getLAN2() {
		return LAN2;
	}

	public Queueing_Centre getGW1() {
		return GW1;
	}
	
	public void setGW1(Queueing_Centre q) {
		this.GW1 = q;
	}

	public Queueing_Centre getGW2() {
		return GW2;
	}
	
	public void setGW2(Queueing_Centre q) {
		this.GW2 = q;
	}

	public void setCpuServers(ArrayList<Queueing_Centre> cpuServers) {
		this.cpuServers = cpuServers;
	}

	public void setMs(ArrayList<Queueing_Centre> ms) {
		this.ms = ms;
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

	public Calendar getCalendar() {
		return calendar;
	}
	
	public Clock getClock() {
		return timer;
	}
	
	public Scheduler getScheduler() {
		return this.scheduler;
	}
	
	public double getAverageMin() {
		return averageMin;
	}

	public void setAverageMin(double averageMin) {
		this.averageMin = averageMin;
	}

	public double getAverageMax() {
		return averageMax;
	}

	public void setAverageMax(double averageMax) {
		this.averageMax = averageMax;
	}
	
	public ArrayList<Integer> getXWANs() {
		return this.xWANs;
	}
	
	public void setXWANs(ArrayList<Integer> x) {

		for(int i = 0; i < x.size(); i++){
			this.xWANs.add(x.get(i));
		}
	}
	
public void getThroughputGraph(Simulation tempSim){
		
		double[] value = new double[tempSim.getXWANs().size()];
		HistogramDataset dataset = new HistogramDataset();
		 
		for(int z = 0; z < tempSim.getXWANs().size(); z++){
			 
			value[z] = tempSim.getXWANs().get(z);
		}

		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Histogram",value,50);
		PlotOrientation orientation = PlotOrientation.VERTICAL; 
	           
		JFreeChart chart = ChartFactory.createHistogram("Throughput Observated at WAN (IS_Centre) for " + CommonVariables.numberOfClient + " CLIENTs", 
														"JOBs Observated", "JOB Occurency", dataset, orientation, false, false, false);

		ChartFrame frameT = new ChartFrame("Troughput",chart);
		frameT.setSize(800,800);
		frameT.setLocationRelativeTo(null);
		frameT.setVisible(true);
	}
}