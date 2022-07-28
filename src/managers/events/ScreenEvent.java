package managers.events;

import java.awt.Event;

import connection.Response;

/**
 * This class represents an event raised when the client receive a response 
 * for a request to remote screen.
 * @author antessio
 *
 */
public class ScreenEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8922026358551518406L;

	private String message;
	private Response response;
	 /**
     * Create a new Event. 
     * @param message The message.
     * @param id 
     * @param response The response received.
     */
    public ScreenEvent(String message, int id, Response response){
        super(message,id,response);
        this.message = message;
        this.response=response;
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
}
