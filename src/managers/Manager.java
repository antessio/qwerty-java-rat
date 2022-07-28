package managers;

import java.util.EventListener;

import managers.exceptions.ManagerException;

import connection.Request;
import connection.Response;
import connection.exceptions.RequestException;

/**
 * This is the common interface for managers. A manager is a class that handles the requests and returns 
 * the  replies to the upper level, in the method "handle" will be controlled the synchronization with 
 * the  response or the event for the asynchronous mode. 
 * @author antessio
 *
 */
public interface Manager{
	
	
	public Response handle(String command, String[] parameters) throws ManagerException;

	public void addEventListener(EventListener listener);
	
}
