/**
	MIS Project, Clock Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;

public class Clock {
	
	private double realTimeStart;
	private double realTimeStop;
	private double simulationTime;
	
	public Clock() {
		
		this.simulationTime = 0.0;
	}
	
	public void startClock() {
		this.realTimeStart = System.currentTimeMillis();
	}
	
	public void setSimulationTime(double x) {
		this.simulationTime = x;
	}
	
	public void plusTime(double delta) {
		this.simulationTime = this.simulationTime + delta;
	}
	
	public double getSimulationTime() {
		return this.simulationTime;
	}
	
	public double getRealTime() {
	
		this.realTimeStop = System.currentTimeMillis();
		return this.realTimeStop - this.realTimeStart;
	}
	
	public Clock clone() {
		
		Clock c = new Clock();
		c.setSimulationTime(this.getSimulationTime());
		
		return c;
	}
	
	public Clock copyClock(Clock c) {
		this.setSimulationTime(c.getSimulationTime());
		return this;
	}
}
