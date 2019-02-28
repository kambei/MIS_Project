/**
	MIS Project, Generator Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package generators;

public class Generator {

	private String type;
	private long x;
	private double avg = 0.0;
	
	public Generator(String t){
		
		this.type = t;
	}
	
	public double getNextNumber(){
		return 0.0;
	}
	
	public String getType(){
		return type;
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
	
	public long getNextSeed(){
		return 0;
	}
	
	public long getSeed(){
		return this.x;
	}

	public double getAverage(){
		return avg;
	}

	public void setAverage(double avg){
		this.avg = avg;
	}
}
