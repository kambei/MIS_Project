/**
	MIS Project, Calendar Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;
import java.util.ArrayList;

public class Calendar {

	private int dimension;
	private ArrayList<Event> eventList;

	public Calendar (int n) {
		
		this.dimension = n;
		this.eventList = new ArrayList<Event>();
		int counter = 0;
		int numberID = 0;
	
		for(int i = 0; i < (5*this.dimension) + 8; i++) {
			this.eventList.add(new Event(-1,Event.INFINITY));
		}
		
		for (int i = 0; i < (5*this.dimension); i++) {
			
			counter ++;
			setCalendar (i, new Event(numberID, Event.INFINITY));
			
			if(counter == this.dimension) {
				counter = 0;
				numberID++;
			}
		}
		
		setCalendar ((5*dimension), new Event(5, Event.INFINITY));
		setCalendar ((5*dimension)+1, new Event(6, Event.INFINITY));
		setCalendar ((5*dimension)+2, new Event(7, Event.INFINITY));
		setCalendar ((5*dimension)+3, new Event(7, Event.INFINITY));
		setCalendar ((5*dimension)+4, new Event(7, Event.INFINITY));
		setCalendar ((5*dimension)+5, new Event(8, Event.INFINITY));
		setCalendar ((5*dimension)+6, new Event(8, Event.INFINITY));
		setCalendar ((5*dimension)+7, new Event(8, Event.INFINITY));
	}
	
	public void setCalendar(int position, Event e) {
		this.eventList.set(position, e);
	}
	
	public Calendar copyCalendar(Calendar c) {
		
		this.eventList.clear();
		this.dimension = c.dimension;
		
		for(int i = 0;i<c.getCalendar().size();i++){
			//this.setCalendar(i, c.getEvent(i));
			this.eventList.add(new Event(c.getCalendar().get(i).getID(),c.getCalendar().get(i).getTime()));
		}
		
		return this;
	}

	public ArrayList<Event> getCalendar() {
		return this.eventList;
	}
	
	public int getSize() {
		return this.eventList.size();
	}
	
	public Event getEvent(int i) {
		return this.eventList.get(i);
	}
	
	public void viewCalendar() {
		
		String calendar = "";
		for (int i=0; i < eventList.size(); i++) {
			calendar = calendar+eventList.get(i).toString() + "\n";
		}
		
		System.out.println(calendar);
	}
	
	public Calendar clone() {
		
		Calendar cal = new Calendar(this.dimension);
		
		for(int i = 0;i<this.getSize();i++){
			cal.setCalendar(i, new Event(this.getEvent(i).getID(),this.getEvent(i).getTime()));
		}
		
		cal.dimension = this.dimension;
		
		return cal; 
	}
}
