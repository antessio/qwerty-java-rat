package connection.scheduler.exceptions;

public class SchedulerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7784942839719540331L;
	
	public SchedulerException(String msg){
		super(msg);
	}
	
	public SchedulerException(String msg, Throwable ex){
		super(msg,ex);
	}

}
