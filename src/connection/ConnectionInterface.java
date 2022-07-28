package connection;

import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;

/**
 * This class defines the unique interface for the Connection. This allow to redefine the kind of connection, the protocol and
 * more things hiding the implementation.
 * @author antessio
 *
 */
public interface ConnectionInterface {

	/**
	 * Opens the connection
	 */
	public void open();
	
	/**
	 * If connection is open
	 * @return <c>true</c> if open, <c>false</c> otherwise
	 */
	public boolean isOpen();
	
	/**
	 * Closes the connection.  
	 * @throws ConnectionException  (Exception throwing is delegated to the implementation)
	 */
	public void close() throws ConnectionException;
	

	/**
	 * This method creates the real connection. 
	 * @return <c>true</c> if connection is done, <c>false</c> otherwise
	 * @throws ConnectionException (Exception throwing is delegated to the implementation)
	 */
	public boolean connect() throws ConnectionException;
	
	/**
	 * If connection is done.
	 * @return <c>true</c> if connected, <c>false</c> otherwise.
	 */
	public boolean isConnected();
	
	
	/**
	 * Sends an object through connection.  
	 * @param obj The object to send
	 * @throws ProtocolException (Exception throwing is delegated to the implementation)
	 * @throws ConnectionException (Exception throwing is delegated to the implementation)
	 */
	public void send(Object obj) throws ProtocolException, ConnectionException;
	
	/**
	 * Receives an object from connection.
	 * @return The object received.
	 * @throws ProtocolException (Exception throwing is delegated to the implementation)
	 * @throws ConnectionException (Exception throwing is delegated to the implementation)
	 */
	public Object receive() throws ProtocolException, ConnectionException;
	
}
