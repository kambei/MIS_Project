/**
	MIS Project, Hyper Exponential Generator Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package generators;
import commons.*;

public class HyperExpGen extends Generator {
	
	private ExpGen eG;
	private RandomGen rand;
	private double p;
	private double average;
	
	public HyperExpGen(long seed,double pN,double avg){
	
		super("Hyper Exp Generator");
		this.eG = new ExpGen(new UniformGen(new RandomGen(seed)),1);
		this.rand = new RandomGen(CommonVariables.seedRandom);
		this.p = pN; 
		this.average = avg;
	}
	
	public double getNextNumber(){
		
		double x = eG.getNextNumber();
		double r = rand.getNextNumber();
		
		if(r <= p){
			x = (average/2*p)*x;
		}else{
			x = (average/2*(1-p))*x;
		}
		
		return x;
	}
	
	public void resetGen(long seed){
		this.eG.resetGen(seed);
		this.rand.resetGen(CommonVariables.seedRandom);
	}
	
	public long getSeed(){
		return this.eG.getSeed();
	}
	
	public void setAverage(double avg){
		this.average = avg;
	}
	
	public long getNextSeed(){
		return (long)eG.getNextSeed();
	}
}
