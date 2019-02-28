/**
	MIS Project, CPU Client Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package centers;
import generators.Generator;

public class CPUClient_Centre {
	
	private int numberCentre;
	private Generator gen;
	
	public CPUClient_Centre(int number, Generator gen) {
		
		this.numberCentre = number;
		this.gen = gen;
	}

	public int getNameCentre() {
		return numberCentre;
	}
	
	public Generator getGenerator() {
		return this.gen;
	}

	public void setNameCentre(int number) {
		this.numberCentre = number;
	}
	
	public double getServiceTime() {
		return this.gen.getNextNumber();
	}
	
	public CPUClient_Centre clone() {
		
		CPUClient_Centre cpuc = new CPUClient_Centre(this.numberCentre,this.gen);
		
		return cpuc;
	}
	
	public CPUClient_Centre copyCentre(CPUClient_Centre t) {
		
		this.numberCentre = t.getNameCentre();
		this.gen = t.getGenerator();
		
		return this;
	}
}