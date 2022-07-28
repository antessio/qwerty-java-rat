package buffers.exceptions;

public class BufferException extends Exception{

	public BufferException(String msg){
		super(msg);
	}
	
	public BufferException(String msg, Throwable e){
		super(msg,e);
	}
}
