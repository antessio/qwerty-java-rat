package connection.receiver;

import buffers.BufferFactory;
import buffers.exceptions.BufferException;
import managers.ProcessType;
import connection.ConnectionInterface;
import connection.Request;
import connection.Response;
import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;

/**
 * This class allows to "redirect" response to the appropriate buffer and associate it with 
 * a request. 
 * @author antessio
 *
 */
public class Receiver extends AbstractReceiver implements Runnable{

	private boolean running=true;
	private boolean isError=false;
	private String error="";
	public Receiver(ConnectionInterface connection) {
		super(connection);
	}
	

	@Override
	public void run() {
		
		while(running){
			try {
				Response response=(Response)connection.receive();
				switchResponse(response);
			} catch (ProtocolException e) {
				running=false;
				error=e.getMessage();
				isError=true;
				e.printStackTrace();
			} catch (ConnectionException e) {
				running=false;
				error=e.getMessage();
				isError=true;
			}
		}
	}
	
	/**
	 * This method reads the response, retrieves the request and inserts it in the buffer associated.
	 * @param response Received response.
	 */
	private void switchResponse(Response response){
		int processType=response.getProcessType().getType();
		try {
			Request request=null;
			switch(processType){
				case ProcessType.COMMANDS:
					request=BufferFactory.getFactory(response.getProcessType()).findRequestById(response.getResponseId());
					BufferFactory.getFactory(response.getProcessType()).add(request, response);
					break;
				case ProcessType.FILES:
					request=BufferFactory.getFactory(response.getProcessType()).findRequestById(response.getResponseId());
					BufferFactory.getFactory(response.getProcessType()).add(request, response);
					//the file requests are asynchronous, the client leaves the message and when 
					//observe a change, checks if that is the response it was waiting for
					setChanged();
					notifyObservers(response);
					break;
				case ProcessType.SCREEN:
					request=BufferFactory.getFactory(response.getProcessType()).findRequestById(response.getResponseId());
					BufferFactory.getFactory(response.getProcessType()).add(request, response);
					//see file requests
					setChanged();
					notifyObservers(response);
					break;
				case ProcessType.CONNECTION:
					//when arrives a response to a connection request, it notifies to the observer
					//that the message arrived.
					setChanged();
					notifyObservers(response);
					break;
			}
		} catch (BufferException e) {
			error=e.getMessage();
			isError=true;
			running=false;
		}
		
	}

	/**
	 * If there was an error the thread is stopped and this method will return the cause of the error.
	 * @return The message of error that makes the thread stop.
	 */
	public String getError(){
		return error;
	}
	/**
	 * When something goes wrong with the scheduler thread, it will stopped and this method will
	 * return true
	 * @return <c>true</c> if an error occurred, <c>false</> if everything goes well.
	 */
	public boolean isError(){
		return isError;
	}
	
	/**
	 * Stops receive responses. 
	 */
	public void stopReceive(){
		running=false;
	}

}
