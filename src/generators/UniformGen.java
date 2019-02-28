/**
	MIS Project, Uniform Generator Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package generators;

public class UniformGen extends Generator {

	private RandomGen randG;
	
	public UniformGen(RandomGen g){
		
		super("Uniform Generator");
		this.randG = g;
	}
	
	public double getNextNumber(){
		return randG.getNextNumber() / ((double)randG.getModule());
	}
	
	public void resetGen(long seed){
		this.randG.resetGen(seed);
	}
	
	public long getSeed(){
		return this.randG.getSeed();
	}
	
	public long getNextSeed(){
		return (long)randG.getNextNumber();
	}
}