package connection.scheduler;

import managers.ProcessType;
import buffers.exceptions.BufferException;
import connection.ConnectionInterface;
import connection.Request;
import connection.Response;
import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;
import connection.scheduler.exceptions.SchedulerException;

public class SchedulerServer extends AbstractScheduler{

	private boolean running=true;
	private boolean veryhigh=true;
	private boolean high=true;
	private boolean medium=true;
	private boolean low=true;
	private int contVERYHIGH=0;
	private int contHIGH=0;
	private int contMEDIUM=0;
	private int contLOW=0;
	private String error;
	private boolean isError;
	
	public SchedulerServer(ConnectionInterface connection) {
		super(connection);
	}

	@Override
	/**
	 * This method insert the request in the related buffer. 
	 * @throws SchedulerException When something wrong occurs, adding into the buffer.
	 * @param request Request you want to schedule. 
	 */
	public void scheduleRequest(Request request) throws SchedulerException {
		int processType=request.getProcessType().getType();
		try{
			switch(processType){
				case ProcessType.COMMANDS:
					bufferCommands.add(request);
				break;
				case ProcessType.FILES:
					bufferFiles.add(request);
				break;
				case ProcessType.SCREEN:
					bufferScreen.add(request);
				break;
			}
		}catch(BufferException ex){
			throw new SchedulerException(ex.getMessage(),ex);
		}
	}

	/**
	 * Basing on process type and the kind of command return a level of priority.
	 * @param request The request to schedule
	 * @return One of constants level of priority
	 */
	private int getPriority(Request request){
		int processType=request.getProcessType().getType();
		switch(processType){
			case ProcessType.COMMANDS:
				if(request.getCommand().equals(Request.PING))
					return PRIORITY_LOW;
				return PRIORITY_MEDIUM;
			case ProcessType.FILES:
				if(request.getCommand().equals(Request.DOWNLOAD) || request.getCommand().equals(Request.UPLOAD))
					return PRIORITY_VERY_HIGH;
				return PRIORITY_HIGH;
			case ProcessType.SCREEN:
				return PRIORITY_VERY_HIGH;
			default: 
				return PRIORITY_LOW;
		}
	}

	/**
	 * USED JUST FOR TESTS
	 * @return
	 */
	public Request testNext(){
		return nextRequest();
	}
	/**
	 * This method handle the priority with which request are sent
	 * @return Next request according to the scheduling policy.
	 */
	private Request nextRequest(){
		Request next=null;
		//check if there are request to handle
		if(bufferScreen.isEmpty() && bufferFiles.isEmpty() && bufferCommands.isEmpty())
			return null;
		//first check bufferScreen
		if(contVERYHIGH<PRIORITY_VERY_HIGH && !bufferScreen.isEmpty()){
				//it will handle first six screen request if there are some
				contVERYHIGH++;
				return bufferScreen.getNext();
		}else if(contHIGH<PRIORITY_HIGH && !bufferFiles.isEmpty()){
				//if there are no VERYHIGH requests or is reached the limit this will handle four files request 
				contHIGH++;
				return bufferFiles.getNext();
			
		}else if(contMEDIUM<PRIORITY_MEDIUM && !bufferCommands.isEmpty()){
				contMEDIUM++;
				return bufferCommands.getNext();
			
		}else{
			contMEDIUM=0;
			contHIGH=0;
			contVERYHIGH=0;
		}
		return nextRequest();
	}
	@Override
	public void run() {
		while(running){
			try {
				Request request = nextRequest();
				Response response = new Response();
				response.setProcessType(request.getProcessType());
				response.setResponseId(request.getIdRequest());
				response.setSender(request.getSender());
				String command="";
				if(request.getProcessType().getType()==ProcessType.CONNECTION){
					command=request.getCommand();
					if(command.equals(Request.PING)){
						response.setReceived(Response.PONG);
						connection.send(response);
					}
				}
				if(request.getProcessType().getType()==ProcessType.COMMANDS){
					
				}
				if(request.getProcessType().getType()==ProcessType.FILES){
					
				}
				if(request.getProcessType().getType()==ProcessType.SCREEN){
					
				}
				
			
					
			} catch (ProtocolException e) {
				running=false;
				isError=true;
				error=e.getMessage();
			} catch (ConnectionException e) {
				running=false;
				isError=true;
				error=e.getMessage();
			}
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
	 * This method stops the scheduler thread.
	 */
	public void stopSchedule(){
		running=false;
	}

}
