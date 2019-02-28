/**
	MIS Project, Event Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;

public class Event {
	
	public final static int NUM_EVENT_TYPES = 8;
	public final static int END_TERM = 0;
	public final static int END_CPC = 1;
	public final static int END_LAN1 = 2;
	public final static int END_WAN = 3;
	public final static int END_LAN2 = 4;
	public final static int END_GW1 = 5;
	public final static int END_GW2 = 6;
	public final static int END_SPC = 7;
	public final static int END_MS = 8;
	public final static double INFINITY = Double.MAX_VALUE;
	
	private int eventID;
	private double eventTime;
	
	public Event (int ID, double time) {
		
		this.eventID = ID;
		this.eventTime = time;
	}
	
	public int getID() {
		return this.eventID;
	}
	
	public void setID(int id) {
		this.eventID = id;
	}
	
	public double getTime() {
		return this.eventTime;
	}
	
	public String toString() {
		return "Event: "+this.eventID+" ; "+this.eventTime;
	}
}