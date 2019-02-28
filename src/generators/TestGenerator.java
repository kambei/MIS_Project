/**
	MIS Project, Test Generator Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package generators;
import java.io.*;
import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;

public class TestGenerator {

	public TestGenerator(){}
	
	public JFreeChart Test(Generator gen){
		
		 double[] value = new double[1000000];
	     double average = 0.0;
	     double variance = 0.0;
	     double min = 999999;
	     double max = 0;
		 HistogramDataset dataset = new HistogramDataset();
		 
		 for(int i = 0;i<1000000;i++){
			 
			 value[i] = gen.getNextNumber();
			 average = average + value[i];
			 if (value[i] < min) {
				 min = value[i];
			 }else if (value[i] > max){
				 max = value[i];
			 } 
		 }
		 average = average / 1000000;
	     
		 for(int i = 0;i<1000000;i++){
			 variance = variance + Math.pow((value[i] - average), 2);
		 }
		 variance = variance / (1000000 - 1);
		 
		 System.out.println(gen.getType()+" Min: "+min);
	     System.out.println(gen.getType()+" Max: "+max);
	     System.out.println(gen.getType()+" Average: "+average);
	     System.out.println(gen.getType()+" Variance: "+variance + "\n");
	     
	     dataset.setType(HistogramType.FREQUENCY);
	     dataset.addSeries("Histogram",value,100);
	     PlotOrientation orientation = PlotOrientation.VERTICAL; 
	           
	     JFreeChart chart = ChartFactory.createHistogram(gen.getType(), "Values", "Occurency", dataset, orientation, false, false, false);
	     
	     try {
	    	 ChartUtilities.saveChartAsPNG(new File(".//graphs//"+gen.getType()+".png"), chart, 1024, 768);
	     } catch (IOException e) {}
	     
	     return chart;
	}
}
