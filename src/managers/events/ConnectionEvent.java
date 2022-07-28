package managers.events;

import java.awt.Event;

import connection.Response;
import connection.ServerInfo;

/**
 * This class represents an event raised when the client receive a connection.
 * @author antessio
 *
 */
public class ConnectionEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8922026358551518406L;

	private String message;
	private Response response;
	private ServerInfo serverInfo;
	 /**
     * Create a new Event. 
     * @param message The message.
     * @param id 
     * @param response The response received.
     */
    public ConnectionEvent(String message, int id, Response response){
        super(message,id,response);
        this.message = message;
        this.response=response;
    }
    /**
     * Create a new Event. 
     * @param message The message.
     * @param id 
     * @param serverInfo The info about the server received.
     */
    public ConnectionEvent(String message, int id, ServerInfo serverInfo){
        super(message,id,serverInfo);
        this.message = message;
        this.serverInfo=serverInfo;
    }
    
    
	
     /**
     * Create a new Event. 
     * @param message The message.
     * @param id 
     */
    public ConnectionEvent(String message, int id){
        super(message,id,null);
        this.message = message;
    }
	/**
     * Returns the message.
     * @return Message
     */
    public String getMessage(){
    	return message;
    }
    
    /**
     * Returns the response received.
     * @return Response
     */
    public Response geteResponse(){
    	return response;
    }
    
    /**
     * Returns the info about the server connected.
     * @return ServerInfo
     */
    public ServerInfo getServerInfo(){
    	return serverInfo;
    }
}
