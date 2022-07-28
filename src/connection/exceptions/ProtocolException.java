package connection.exceptions;

public class ProtocolException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7127009430612668133L;

	public ProtocolException(String msg){
		super(msg);
	}
	
	public ProtocolException(String msg, Throwable ex){
		super(msg,ex);
	}
}
