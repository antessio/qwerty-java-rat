package connection.exceptions;

public class StateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8645994341465398812L;
	
	public StateException(String msg){
		super(msg);
	}
	
	public StateException(String msg, Throwable ex){
		super(msg,ex);
	}

}
