/**
	MIS Project, Exponential Generator Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package generators;

public class ExpGen extends Generator {
	
	private UniformGen uG;
	private double average;
	
	public ExpGen(UniformGen gen, double avg){
		
		super("Exponential Generator");
		this.uG = gen;
		this.average = avg;
	}
	
	public double getNextNumber(){
		
		return -this.average * Math.log(uG.getNextNumber());
	}
	
	public void resetGen(long seed){
		this.uG.resetGen(seed);
	}
	
	public long getSeed(){
		return this.uG.getSeed();
	}
	
	public void setAverage(double avg){
		this.average = avg;
	}
	
	public long getNextSeed(){
		return (long)uG.getNextSeed();
	}
}
