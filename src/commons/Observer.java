/**
	MIS Project, Observer Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;
import centers.*;
import generators.Generator;
import generators.RandomGen;
import java.util.ArrayList;

public class Observer {
	
	private Scheduler sched;
	private Simulation sim;
	private ArrayList<Job> jobs;
	private Calendar calendar;
	private Clock clk;
	private Queueing_Centre GW1;
	private Queueing_Centre GW2;
	private Queueing_Centre SPc1;
	private Queueing_Centre SPc2;
	private Queueing_Centre SPc3;
	private Queueing_Centre Ms1;
	private Queueing_Centre Ms2;
	private Queueing_Centre Ms3;
	private ArrayList<TERM_Centre> terminals;
	private ArrayList<CPUClient_Centre> cpuClients;
	private IS_Centre LAN1;
	private IS_Centre WAN;
	private IS_Centre LAN2;
	private RandomGen randObs = null;
	private ArrayList<Integer> xWANs;
	private int obs;
	
	public Observer(Scheduler scheduler, Simulation simulation, int obs){
		
		this.sched = scheduler;
		this.sim = simulation;
		this.obs = obs;
		this.randObs = new RandomGen(CommonVariables.seedRandObs);
		this.randObs.setModule(50);
		this.jobs = new ArrayList<Job>();
		this.calendar = new Calendar(CommonVariables.numberOfClient);
		this.clk = new Clock();
		this.GW1 = new Queueing_Centre(new Generator(""),CommonVariables.LIFOQueue);
		this.GW2 = new Queueing_Centre(new Generator(""),CommonVariables.LIFOQueue);
		this.SPc1 = new Queueing_Centre(new Generator(""),CommonVariables.FIFOQueue);
		this.SPc2 = new Queueing_Centre(new Generator(""),CommonVariables.FIFOQueue);
		this.SPc3 = new Queueing_Centre(new Generator(""),CommonVariables.FIFOQueue);
		this.Ms1 = new Queueing_Centre(new Generator(""),CommonVariables.RANDQueue);
		this.Ms2 = new Queueing_Centre(new Generator(""),CommonVariables.RANDQueue);
		this.Ms3 = new Queueing_Centre(new Generator(""),CommonVariables.RANDQueue);
		this.terminals = new ArrayList<TERM_Centre>();
		this.cpuClients = new ArrayList<CPUClient_Centre>();
	}
	
	public void observ(int N) {

		this.sched.simulateRun(obs);
		CommonVariables.timeStab = this.sched.getClock().getSimulationTime();
		if(CommonVariables.printObs)System.out.println(">>>> Simulation Time: " + this.sched.getClock().getSimulationTime() + " sec, after --" 
				+ obs + "-- observation to get the STABLE STATE!!!  ");
		
		this.sched.resetReturns();
		this.getStabState();
		
		int tempVar = N % (N+1);
		double tempNextNumb = this.randObs.getNextNumber();
		
		if (tempNextNumb % 5 == 0 || tempNextNumb % 2 == 0){
			tempNextNumb++;
		}
		
		if ((tempVar + tempNextNumb) % 2 == 1){
			this.randObs.resetGen((long) (tempNextNumb + tempVar));
		}else{
			this.randObs.resetGen((long) (tempNextNumb + (111 * tempVar) + 1));
		}
		
		int n = 0;
		double y = 0.0;
		double y_temp = 0.0;
		double sDelta = 0.0;
		double sGamma = 0.0;
		double s11 = 0.0;
		double s22 = 0.0;
		double s12s21 = 0.0;
		double sFinal = 0.0;
		double f = 0.0;
		double d = 0.0;
		double deltaMin = 0.0;
		double deltaMax = 0.0;
		double gammaMin = 0.0;
		double gammaMax = 0.0;
		
		ArrayList<Double> y_ret = new ArrayList<Double>();
		ArrayList<Integer> n_rand = new ArrayList<Integer>();
		
		this.xWANs = new ArrayList<Integer>();
		
		for(int i = 1; i <= CommonVariables.numberOfRun; i++){	
			
			int o = (int)(randObs.getNextNumber()%51) + 50;
			n += o;
			n_rand.add(o);
			
			if(CommonVariables.printObs)System.out.println("\n----------------RUN number: " + i + "  ---random nj: " + o);
			
			this.setStabState(N);
			this.sched.simulateRun(o);
			
			if(N == CommonVariables.throughputObs){
				xWANs.add(this.sched.getXWAN());
				this.sched.resetXWAN();
			}
			
			for(int j = 0; j < this.sched.getReturns().size(); j++){				
				if(CommonVariables.printObsReturns)System.out.println("**RETURN "+j+" is: "+this.sched.getReturns().get(j));
				y_temp += this.sched.getReturns().get(j);
			}
			
			double temp = 0.0;
			for (int k = 0; k < this.sched.getReturns().size(); k++){
				temp += this.sched.getReturns().get(k);
				if(CommonVariables.printObsReturns)System.out.println(this.sched.getReturns().get(k));
			}
			if(CommonVariables.printObsReturns) {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Somma dei RETURNs: " + temp);
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Media: " + (temp/this.sched.getReturns().size()));
			}
			
			y_ret.add(y_temp);
			y_temp = 0.0;
			
			this.sched.resetReturns();
		}
		
		this.sim.setXWANs(xWANs);
		
		for(int i = 0; i < y_ret.size(); i++ ) {
			y += y_ret.get(i);
		}
		
		y = y / CommonVariables.numberOfRun;
		n = n / CommonVariables.numberOfRun;
		f = y / n;
		
		for (int i = 0; i < y_ret.size(); i++){
			s11 += Math.pow((y_ret.get(i) - y), 2);
		}
		s11 = s11 / (CommonVariables.numberOfRun - 1);
		sDelta = Math.sqrt(s11);		
		
		for (int i = 0; i < n_rand.size(); i++){
			s22 += Math.pow((n_rand.get(i) - n), 2);
		}
		s22 = s22 / (CommonVariables.numberOfRun - 1);
		sGamma = Math.sqrt(s22);
		
		for (int i = 0; i < CommonVariables.numberOfRun; i++){
			s12s21 += (y_ret.get(i) - y) * (n_rand.get(i) - n);
		}
		
		s12s21 = s12s21 / (CommonVariables.numberOfRun - 1);
		sFinal = s11 - (s12s21 * 2 * f) + (s22 * (Math.pow(f, 2)));
		sFinal = Math.sqrt(sFinal);
		
		d = sFinal / (n * Math.sqrt(CommonVariables.numberOfRun));
		
		deltaMin = y - ((sDelta/(Math.sqrt(CommonVariables.numberOfRun)) * CommonVariables.u_alfa));
		deltaMax = y + ((sDelta/(Math.sqrt(CommonVariables.numberOfRun)) * CommonVariables.u_alfa));
		gammaMin = n - ((sGamma/(Math.sqrt(CommonVariables.numberOfRun)) * CommonVariables.u_alfa));
		gammaMax = n + ((sGamma/(Math.sqrt(CommonVariables.numberOfRun)) * CommonVariables.u_alfa));
		
		this.sim.setAverageMin(f - (d * CommonVariables.u_alfa));
		this.sim.setAverageMax(f + (d * CommonVariables.u_alfa));
		
		if(CommonVariables.printObs) {
			
			System.out.println("---------------------CALCULATED Confidence Intervals for Average!!!");
			System.out.println("@@@@ s11: " + s11);
			System.out.println("@@@@ s22: " + s22);
			System.out.println("@@@@ s12_s21: " + s12s21);
			System.out.println("@@@@@@@@@@@ sFinal^2: "+ Math.pow(sFinal,2));
			System.out.println("@@@@@@@@@@@@@ sFinal: " + sFinal);
			System.out.println("##### n_signed: " + n);
			System.out.println("##### y_signed: " + y);
			System.out.println("##### f_signed: " + f);
			System.out.println("##### Delta MIN: " + deltaMin + " ##### Delta MAX: " + deltaMax);
			System.out.println("##### Gamma MIN: " + gammaMin + " ##### Gamma MAX: " + gammaMax);
			System.out.println("##### Average MIN: " + this.sim.getAverageMin() + " ##### Average MAX: " + this.sim.getAverageMax());
			System.out.println("----------------------------------------------------------------------");
		}
	}	
	
	public void getStabState(){
	
		for(int i = 0; i < this.sched.getJobs().size(); i++){
			this.jobs.add(this.sched.getJobs().get(i).clone());
		}
		
		this.calendar = this.sched.getCalendar().clone();
		
		this.GW1 = this.sched.getGW1().clone();
		this.GW2 = this.sched.getGW2().clone();
		
		this.SPc1 = this.sched.getCpuServers().get(0).clone();
		this.SPc2 = this.sched.getCpuServers().get(1).clone();
		this.SPc3 = this.sched.getCpuServers().get(2).clone();
		
		this.Ms1 = this.sched.getMs().get(0).clone();
		this.Ms2 = this.sched.getMs().get(1).clone();
		this.Ms3 = this.sched.getMs().get(2).clone();
						
		this.clk = this.sched.getClock().clone();
		
		this.LAN1 = this.sched.getLAN1().clone();
		this.WAN = this.sched.getWAN().clone();
		this.LAN2 = this.sched.getLAN2().clone();
		
		for(int i = 0;i<this.sched.getTerminals().size();i++){		
			this.terminals.add(this.sched.getTerminals().get(i).clone());
		}
		
		for(int i = 0;i<this.sched.getCpuClients().size();i++){
			this.cpuClients.add(this.sched.getCpuClients().get(i).clone());
		}
	}

	public void setStabState(int N) {
				
		ArrayList<Job> js = new ArrayList<Job>();
		
		for(int i = 0; i < this.jobs.size(); i++){
			Job j = new Job(-1,0,0,0);
			j = j.copyJob(this.jobs.get(i));
			js.add(j);
		}
		
		this.sched.setJobs(js);
		
		Calendar c = new Calendar(N);
		c.copyCalendar(this.calendar);
		
		this.sched.setCalendar(c);
		
		Queueing_Centre gw11 = new Queueing_Centre(new Generator(""),CommonVariables.LIFOQueue);
		gw11.copyCentre(this.GW1);
		Queueing_Centre gw22 = new Queueing_Centre(new Generator(""),CommonVariables.LIFOQueue);
		gw22.copyCentre(this.GW2);
		
		this.sched.setGW1(gw11);
		this.sched.setGW1(gw22);
		
		Queueing_Centre spc1 = new Queueing_Centre(new Generator(""),CommonVariables.FIFOQueue);
		Queueing_Centre spc2 = new Queueing_Centre(new Generator(""),CommonVariables.FIFOQueue);
		Queueing_Centre spc3 = new Queueing_Centre(new Generator(""),CommonVariables.FIFOQueue);
		Queueing_Centre ms1 = new Queueing_Centre(new Generator(""),CommonVariables.RANDQueue);
		Queueing_Centre ms2 = new Queueing_Centre(new Generator(""),CommonVariables.RANDQueue);
		Queueing_Centre ms3 = new Queueing_Centre(new Generator(""),CommonVariables.RANDQueue);
		
		spc1.copyCentre(this.SPc1);
		spc2.copyCentre(this.SPc2);
		spc3.copyCentre(this.SPc3);
		
		ms1.copyCentre(this.Ms1);
		ms2.copyCentre(this.Ms2);
		ms3.copyCentre(this.Ms3);

		ArrayList<Queueing_Centre> cpuS = new ArrayList<Queueing_Centre>();
		cpuS.add(spc1);
		cpuS.add(spc2);
		cpuS.add(spc3);
		
		ArrayList<Queueing_Centre> msS = new ArrayList<Queueing_Centre>();
		msS.add(ms1);
		msS.add(ms2);
		msS.add(ms3);
		
		this.sched.setCpuServers(cpuS);
		this.sched.setMs(msS);
		
		this.sched.setLAN1(LAN1);
		this.sched.setWAN(WAN);
		this.sched.setLAN2(LAN2);
		
		ArrayList<TERM_Centre> tN = new ArrayList<TERM_Centre>();
		for(int i = 0;i<this.terminals.size();i++){
			TERM_Centre tx = new TERM_Centre(-1,new Generator(""));
			tx.copyCentre(this.sched.getTerminals().get(i));
			tN.add(tx);
		}
		
		this.sched.setTerminals(tN);
		
		ArrayList<CPUClient_Centre> cN = new ArrayList<CPUClient_Centre>();
		for(int i = 0;i<this.cpuClients.size();i++){
			CPUClient_Centre cx = new CPUClient_Centre(-1,new Generator(""));
			cx.copyCentre(this.sched.getCpuClients().get(i));
			cN.add(cx);
		}
		
		this.sched.setCpuClients(cN);
		
		Clock tr = new Clock();
		tr.copyClock(this.clk);
		
		this.sched.setClock(tr);
		
		for(int i = 0; i < N; i++){
			
			long t = this.sched.getTerminals().get(i).getGenerator().getNextSeed();
			this.sched.getTerminals().get(i).getGenerator().resetGen(t);
			
			long c1 = this.sched.getCpuClients().get(i).getGenerator().getNextSeed();
			this.sched.getCpuClients().get(i).getGenerator().resetGen(c1);
			
			long l1 = this.sched.getLAN1().getCenters().get(i).getNextSeed();
			this.sched.getLAN1().getCenters().get(i).resetGen(l1);
			
			long w = this.sched.getWAN().getCenters().get(i).getNextSeed();
			this.sched.getWAN().getCenters().get(i).resetGen(w);
			
			long l2 = this.sched.getLAN2().getCenters().get(i).getNextSeed();
			this.sched.getLAN2().getCenters().get(i).resetGen(l2);
		}
		
		long gw1 = this.sched.getGW1().getGenerator().getNextSeed();
		this.sched.getGW1().getGenerator().resetGen(gw1);
		
		long gw2 = this.sched.getGW2().getGenerator().getNextSeed();
		this.sched.getGW2().getGenerator().resetGen(gw2);
		
		this.sched.getGW1().resetQGen();
		this.sched.getGW2().resetQGen();
		
		for(int i = 0; i < CommonVariables.numberOfServer; i++) {
			
			long c2 = this.sched.getCpuServers().get(i).getGenerator().getNextSeed();
			this.sched.getCpuServers().get(i).getGenerator().resetGen(c2);
			
			long m = this.sched.getMs().get(i).getGenerator().getNextSeed();
			this.sched.getMs().get(i).getGenerator().resetGen(m);
			
			this.sched.getCpuServers().get(i).resetQGen();
			this.sched.getMs().get(i).resetQGen();
		}
		
		this.sched.resetRoutingGen();
	}
}