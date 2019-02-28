/**
	MIS Project, Stabilization Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Stabilizator {

	private Scheduler scheduler;
	private Simulation simulation;
	private ArrayList<Double> GordonAvg;
	private ArrayList<Double> GordonVar;
	
	public Stabilizator(Scheduler sch, Simulation sim){
		
		this.scheduler = sch;
		this.simulation = sim;
		this.GordonAvg = new ArrayList<Double>();
		this.GordonVar = new ArrayList<Double>();
	}
	
	public void stabilize(int N) {
		
		CommonVariables.stabilizationObs = 1;

		FileWriter fw1 = null;
		FileWriter fw2 = null;
		
		try {
			fw1 = new FileWriter("Average.ods");
			fw2 = new FileWriter("Variance.ods");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        PrintWriter pw1 = new PrintWriter(fw1);
        PrintWriter pw2 = new PrintWriter(fw2);
	
		ArrayList<Double> avg = new ArrayList<Double>();
		boolean oK = false;
		boolean findObs = false;
		int count = 1;
		double avgSingleRun = 0.0;
		double GordonEstAvg = 0.0;
		double GordonEstVar = 0.0;
		
	    this.scheduler.startClock();
	    
		while (!findObs){
			System.out.println("\n------SIMULATE " + CommonVariables.numberOfRun 
					+ " RUN , for " + CommonVariables.stabilizationObs + " OBSERVATION!!!" );
			
			for(int i = 0; i < CommonVariables.numberOfRun; i++){
				
				if(CommonVariables.printStabRun)System.out.println("\n----------------RUN number: " + i + "  ---Observation number: " + CommonVariables.stabilizationObs);
				
				this.scheduler.simulateRun(CommonVariables.stabilizationObs);
				this.simulation.getClock().setSimulationTime(0.0);
				this.simulation.resetNet(N);
				this.simulation.resetGen(N);
				
				for (int j=0; j < this.scheduler.getReturns().size(); j++){
					avgSingleRun += this.scheduler.getReturns().get(j);
				}
				
				avgSingleRun = avgSingleRun / CommonVariables.stabilizationObs;
				avg.add(avgSingleRun);
				this.scheduler.resetReturns();
				avgSingleRun = 0.0;
			}
			
			for(int i = 0; i < avg.size(); i++){
				GordonEstAvg += avg.get(i);
			}
			
			GordonEstAvg = GordonEstAvg / avg.size();
			GordonAvg.add(GordonEstAvg);
			
			pw1.println(GordonEstAvg);
			pw1.flush();
			
			for(int i = 0; i < avg.size(); i++){
				GordonEstVar += Math.pow((avg.get(i) - GordonEstAvg), 2); 
			}
			
			GordonEstVar = GordonEstVar / (avg.size() - 1) ;
			GordonVar.add(GordonEstVar);
			
			pw2.println(GordonEstVar);
			pw2.flush();
			
			if(CommonVariables.numberOfClient == 12){
				
				CommonVariables.stopStab = 100; 	
				CommonVariables.intervalAvg = 0.1; 
				CommonVariables.intervalVar = 0.1;
			
			} else if(CommonVariables.numberOfClient == 24) {
				
				CommonVariables.stopStab = 100; 	
				CommonVariables.intervalAvg = 0.1; 
				CommonVariables.intervalVar = 0.1;

			} else if(CommonVariables.numberOfClient == 30) {
				
				CommonVariables.stopStab = 100; 	
				CommonVariables.intervalAvg = 0.15; 
				CommonVariables.intervalVar = 0.1;

			} else if(CommonVariables.numberOfClient == 40) {
				
				CommonVariables.stopStab = 100; 	
				CommonVariables.intervalAvg = 0.3; 
				CommonVariables.intervalVar = 0.1;

			} else if(CommonVariables.numberOfClient == 60) {
				
				CommonVariables.stopStab = 200; 	
				CommonVariables.intervalAvg = 0.6; 
				CommonVariables.intervalVar = 0.1;

			} else if(CommonVariables.numberOfClient == 80) {
				
				CommonVariables.stopStab = 400; 	
				CommonVariables.intervalAvg = 0.8; 
				CommonVariables.intervalVar = 0.1;

			} else if(CommonVariables.numberOfClient == 100) {
				
				CommonVariables.stopStab = 500; 	
				CommonVariables.intervalAvg = 1; 
				CommonVariables.intervalVar = 0.1;

			}
			
			if ((CommonVariables.stabilizationObs > 10 ) 
					&& Math.abs(GordonAvg.get(CommonVariables.stabilizationObs-1) - GordonAvg.get(CommonVariables.stabilizationObs-2)) < CommonVariables.intervalAvg
					&& (GordonEstVar/CommonVariables.stabilizationObs) < CommonVariables.intervalVar) {
				oK = true;
				count++;
				
			} else {
				count = 1;
				oK = false;
			}
			
			if (oK && (count > CommonVariables.stopStab)) {
				findObs = true;
			}
			
			if(CommonVariables.printStabGordon) {
				System.out.println("\nGGGGGGGGGGGGGGGG____FOR_THIS_"+CommonVariables.numberOfRun+"_RUN____GGGGGGGGGGGGGGGGG Gordon Average Estimator: " +GordonEstAvg);
				System.out.println("GGGGGGGGGGGGGGGG____FOR_THIS_"+CommonVariables.numberOfRun+"_RUN____GGGGGGGGGGGGGGGGG Gordon Variance Estimator: " +GordonEstVar);
			}
			
			GordonEstAvg = 0.0;
			GordonEstVar = 0.0;
			avg.clear();
			
			CommonVariables.stabilizationObs++;
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
		
        if(CommonVariables.graphStabState) {
        	this.getStabAverageGraph();
        	this.getStabVarianceGraph(); 
        }
		
        if(CommonVariables.printStabRun) {
        	System.out.println("\n------Stabilization REAL TIME: " + this.scheduler.stopClock()+" sec"); 
        	System.out.println("----------------------------------------------------------------------");
        }
	}
	
	public void getStabAverageGraph(){
		
		XYSeries series = new XYSeries("Average");
		 
		for(int i = 0; i < CommonVariables.stabilizationObs-1; i++){
			 
			Double a = this.GordonAvg.get(i);
			series.add(i,a+0);
		 }
		
		 XYDataset xyDataset = new XYSeriesCollection(series);
		 JFreeChart chart = ChartFactory.createXYLineChart("R Gordon Average Estimator - Stabilization for " + CommonVariables.numberOfClient + " CLIENTs", "Number of Observation (n)", "Sample Average (sec)",xyDataset, PlotOrientation.VERTICAL, false, false, false);
		 ChartFrame frameG = new ChartFrame("XYLine Chart",chart);
		 frameG.setSize(800,800);
		 frameG.setLocationRelativeTo(null);
		 frameG.setVisible(true);
	}
	
	public void getStabVarianceGraph(){
		
		XYSeries series = new XYSeries("Variance");
		 
		for(int i = 0; i < CommonVariables.stabilizationObs-1; i++){
			 
			Double a = this.GordonVar.get(i);
			series.add(i,a+0);
		 }
		
		 XYDataset xyDataset = new XYSeriesCollection(series);
		 JFreeChart chart = ChartFactory.createXYLineChart("R Gordon Variance Estimator - Stabilization for " + CommonVariables.numberOfClient + " CLIENTs", "Number of Observation (n)", "Sample Variance (sec)",xyDataset, PlotOrientation.VERTICAL, false, false, false);
		 ChartFrame frameV = new ChartFrame("XYLine Chart",chart);
		 frameV.setSize(800,800);
		 frameV.setLocationRelativeTo(null);
		 frameV.setVisible(true);
	}
}