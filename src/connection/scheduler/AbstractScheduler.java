package connection.scheduler;

import managers.ProcessType;
import buffers.BufferCommands;
import buffers.BufferConnection;
import buffers.BufferFactory;
import buffers.BufferFiles;
import buffers.BufferScreen;
import connection.ConnectionInterface;
import connection.Request;
import connection.scheduler.exceptions.SchedulerException;

/**
 * This class defines a structure on which the real scheduler will base itself.
 * @author antessio
 *
 */
public abstract class AbstractScheduler extends Thread{
	protected ConnectionInterface connection;
	protected BufferCommands bufferCommands;
	protected BufferFiles bufferFiles;
	protected BufferScreen bufferScreen;
	protected BufferConnection bufferConnection;
	public final static int PRIORITY_VERY_HIGH=7;
	public final static int PRIORITY_HIGH=4;
	public final static int PRIORITY_MEDIUM=4;
	public final static int PRIORITY_LOW=3;
	
	
	
	protected AbstractScheduler(ConnectionInterface connection){
		bufferCommands=(BufferCommands) BufferFactory.getFactory(new ProcessType(ProcessType.COMMANDS));
		bufferFiles=(BufferFiles) BufferFactory.getFactory(new ProcessType(ProcessType.FILES));
		bufferScreen=(BufferScreen) BufferFactory.getFactory(new ProcessType(ProcessType.SCREEN));
		bufferConnection=(BufferConnection)BufferFactory.getFactory(new ProcessType(ProcessType.CONNECTION));
		this.connection=connection;
	}
	
	/**
	 * This method insert the request in the related buffer. 
	 * @throws SchedulerException (Exception throwing is handled by the implementation)
	 * @param request Request you want to schedule. 
	 */
	public abstract void scheduleRequest(Request request) throws SchedulerException;
}
