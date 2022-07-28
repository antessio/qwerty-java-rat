package connection;

import java.io.Serializable;

import managers.ProcessType;

/**
 * This class gives the structure of response. This class is equal to Request but is separated for future modifications.
 * @author antessio
 *
 */
public class Response implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5164125182394414384L;
	public static final String OK="OK";
	public static final String NOK="NOK";
	public static final String PONG="PONG";
	public static final String WAIT = "WAIT";
	public static final String BAD_REQUEST="BADREQUEST";
	
	private ProcessType processType;
	private Object received;
	private int sender; //secondary id
	private int responseId;
	
	
	public Response(ProcessType processType, Object received, int sender,int responseId) {
		this.processType = processType;
		this.received = received;
		this.sender = sender;
		this.responseId = responseId;
	}
	
	public Response(){
		
	}
	public ProcessType getProcessType() {
		return processType;
	}
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}
	public Object getReceived() {
		return received;
	}
	public void setReceived(Object received) {
		this.received = received;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public int getResponseId() {
		return responseId;
	}
	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}

	@Override
	public String toString() {
		return responseId+Request.MSG_SEPARATOR+sender+Request.MSG_SEPARATOR+processType+Request.MSG_SEPARATOR+received;
	}
	@Override
	public boolean equals(Object arg0) {
		Response r=(Response)arg0;
		if(r.getResponseId()!=responseId || r.getSender()!=sender || !r.getProcessType().equals(processType) || !r.getReceived().equals(received))
			return false;
		return true;
	}	
}
