/**
	MIS Project, Random Generator Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package generators;

public class RandomGen extends Generator {

	private long x;
	private long a = 1220703125;
	private long m = 2147483647;
	
	public RandomGen(long seed){
	
		super("Random Generator");
		this.x = seed;
	}
	
	public double getNextNumber(){
		
		this.x = (a*x) % (m + 1);
		return x;
	}
	
	public void resetGen(long seed){
		if(seed % 5 == 0 || seed % 2 == 0){
			if (seed % 2 == 0){
				this.x = seed + 1;
			}else{
				this.x = seed + 2;
			}
		}else{
			this.x = seed;
		}
	}
	
	public long getModule(){
		return m + 1;
	}
	
	public long getSeed(){
		return this.x;
	}
	
	public void setModule(long m){
		this.m = m;
	}
}
