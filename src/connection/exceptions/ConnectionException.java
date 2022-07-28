package connection.exceptions;

public class ConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3060565921864144996L;

	public ConnectionException(String msg){
		super(msg);
	}
	
	public ConnectionException(String msg, Throwable ex){
		super(msg,ex);
	}
}
