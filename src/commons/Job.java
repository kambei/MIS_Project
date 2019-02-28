/**
	MIS Project, Job Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;

public class Job {
	
	private int jobID;
	private int jobClass;
	private double endServiceTime, startServiceTime, responceTime;
	
	public Job(int jobID, double endServiceTime, 
				double startServiceTime, double responceTime) {
		
		this.jobID = jobID;
		this.jobClass = 1;
		this.endServiceTime = endServiceTime;
		this.startServiceTime = startServiceTime;
		this.responceTime = responceTime;
	}

	public int getJobID() {
		return jobID;
	}

	public void setJobID(int jobID) {
		this.jobID = jobID;
	}

	public int getJobClass() {
		return jobClass;
	}

	public void setJobClass(int jobClass) {
		this.jobClass = jobClass;
	}

	public double getEndServiceTime() {
		return endServiceTime;
	}

	public void setEndServiceTime(double endServiceTime) {
		this.endServiceTime = endServiceTime;
	}

	public double getStartServiceTime() {
		return startServiceTime;
	}

	public void setStartServiceTime(double startServiceTime) {
		this.startServiceTime = startServiceTime;
	}
	
	public double getResponceTimeREAL() {
		responceTime = this.endServiceTime - this.startServiceTime;
		return responceTime;
	}
	
	public double getResponceTime() {
		return this.endServiceTime - this.startServiceTime;
	}
	
	public double getResponce() {
		return responceTime;
	}
	
	public void setResponceTime(double time) {
		responceTime = time;
	}

	public void resetJob() {
	
		this.jobClass = 1;
		this.endServiceTime = 0;
		this.startServiceTime = 0;
		this.responceTime = 0;
	}
	
	public Job clone() {
		
		Job j = new Job (this.getJobID(), this.getEndServiceTime(), this.getStartServiceTime(), this.getResponce());
		j.setJobClass(this.jobClass);
		return j;
	}

	public Job copyJob(Job j) {
		
		this.jobID = j.getJobID();
		this.jobClass = j.getJobClass();
		this.endServiceTime = j.getEndServiceTime();
		this.startServiceTime = j.getStartServiceTime();
		this.responceTime = j.getResponce();
		
		return this;
	}
}