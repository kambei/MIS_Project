/**
	MIS Project, TERMINAL Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package centers;
import generators.Generator;

public class TERM_Centre {
	
	private int numberCentre;
	private Generator gen;
	
	public TERM_Centre(int number, Generator gen) {
		
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
	
	public TERM_Centre clone() {
		
		TERM_Centre term = new TERM_Centre(this.numberCentre,this.gen);
		
		return term;
	}
	
	public TERM_Centre copyCentre(TERM_Centre t) {
		
		this.numberCentre = t.getNameCentre();
		this.gen = t.getGenerator();
		
		return this;
	}
}
