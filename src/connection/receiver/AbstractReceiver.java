package connection.receiver;

import java.util.Observable;
import managers.ProcessType;
import buffers.BufferCommands;
import buffers.BufferFactory;
import buffers.BufferFiles;
import buffers.BufferScreen;
import connection.ConnectionInterface;

/**
 * This abstract class gives the structure for a thread receiver. 
 * The thread reads incoming messages and stores them in the appropriate buffer. 
 * The most important function is manage synchronous and asynchronous processes. 
 * Thanks to Observer design pattern, when you need an asynchronous response, 
 * you can leave the requests  in buffer and when the receiver reads the response 
 * notify it to the Observers.  
 * @author antessio
 *
 */
public abstract class AbstractReceiver extends Observable{
	protected ConnectionInterface connection;
	protected BufferCommands bufferCommands;
	protected BufferFiles bufferFiles;
	protected BufferScreen bufferScreen;
	
	/**
	 * Constructor. Sets the connection on which messages will be received and initializes 
	 * the buffers to store responses.
	 * @param connection The connection where messages are exchanged.
	 */
	public AbstractReceiver(ConnectionInterface connection){
		this.connection=connection;
		bufferCommands=(BufferCommands) BufferFactory.getFactory(new ProcessType(ProcessType.COMMANDS));
		bufferFiles=(BufferFiles) BufferFactory.getFactory(new ProcessType(ProcessType.FILES));
		bufferScreen=(BufferScreen) BufferFactory.getFactory(new ProcessType(ProcessType.SCREEN));
	}
	
	

}
