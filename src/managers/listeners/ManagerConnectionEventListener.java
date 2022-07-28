package managers.listeners;

import java.util.EventListener;

import managers.events.ConnectionEvent;



public abstract class ManagerConnectionEventListener implements EventListener{
	
	/**
	 * When the connection is created.
	 * @param evt 
	 */
	public abstract void newConnection(ConnectionEvent evt);
	
	/**
	 * When the closing of connection is done.
	 * @param evt
	 */
	public abstract void connectionClosed(ConnectionEvent evt);
	
	/**
	 * When the connection is unexpectedly closed. 
	 * @param evt
	 */
	public abstract void connectionFault(ConnectionEvent evt);

	/**
	 * When there is a connection incoming.
	 * @param evt
	 */
	public abstract void connectionReceived(ConnectionEvent evt);
}
