package managers.exceptions;

public class ManagerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3817092108295662398L;

	public ManagerException(String msg){
		super(msg);
	}
	
	public ManagerException(String msg, Throwable ex){
		super(msg,ex);
	}
}
