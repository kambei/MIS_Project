/**
	MIS Project, InifiniteService IS_Centre Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package centers;
import generators.*;
import commons.*;
import java.util.ArrayList;

public class IS_Centre {

	private ArrayList<Generator> centers;
	
	public IS_Centre(ArrayList<Generator> gen) {
		
		this.centers = gen;
	}

	public ArrayList<Generator> getCenters() {
		return centers;
	}
	
	public double getServiceTime(Job job) {
		return this.centers.get(job.getJobID()).getNextNumber();
	}	
	
	public IS_Centre clone() {
		
		ArrayList<Generator> g = new ArrayList<Generator>();
		
		for(int i = 0;i < centers.size();i++){
			g.add(centers.get(i));
		}
		
		IS_Centre is = new IS_Centre(g);
		return is;
	}
	
	public void copyCentre(IS_Centre is) {
		
		this.centers.clear();
		
		for(int i = 0;i < is.getCenters().size();i++){
			centers.add(is.getCenters().get(i));
		}
	}
}
