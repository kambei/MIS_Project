/**
	MIS Project, K-Erlang Generator Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package generators;

public class KerlGen extends Generator {
	
	private ExpGen eG;
	private int k;
	
	public KerlGen(long seed,double average,int k){
		
		super(k+"-Erlang Generator");
		this.eG = new ExpGen(new UniformGen(new RandomGen(seed)),average/k);
		this.k = k;
	}
	
	public double getNextNumber(){
		
		double x = 0;
		
		for(int i = 0;i<k;i++){
			x = x + eG.getNextNumber();
		}
		
		return x;
	}
	
	public void resetGen(long seed){
		this.eG.resetGen(seed);
	}
	
	public long getSeed(){
		return this.eG.getSeed();
	}
	
	public void setAverage(double avg){
		this.eG.setAverage(avg);
	}
	
	public long getNextSeed(){
		return (long)eG.getNextSeed();
	}
}
