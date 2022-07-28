package server;

import configuration.ServerConfiguration;
import connection.ConnectionInterface;
import connection.Request;
import connection.ServerConnection;
import connection.ServerState;
import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;
import connection.exceptions.StateException;
import connection.scheduler.SchedulerServer;
import connection.scheduler.exceptions.SchedulerException;

/**
 * This class implements the logic to manage the server. An object of this class is a thread that checks
 * the state of connection and the flow of operation on the server. 
 * @author antessio
 *
 */
public class QwertyServer{
	
	private ServerConfiguration configuration;
	private static QwertyServer instance;
	private ServerConnection connection;
	private ServerState state;
	private ServerController controller;
	private ServerReceiver receiver;
	private SchedulerServer scheduler;
	/**
	 * Singleton method to create server instance. 
	 * @return The single instance of the server.
	 */
	public static QwertyServer getInstance(){
		if(instance==null)
			instance=new QwertyServer();
		return instance;
	}
	
	/**
	 * Constructor. 
	 */
	private QwertyServer(){	
		state=new ServerState(ServerState.OFFLINE);
		controller=new ServerController();
		receiver=new ServerReceiver();
	}
	
	/**
	 * This method set the configuration
	 * @param configuration An object containing configuration parameters.
	 */
	public void configure(ServerConfiguration configuration){
		this.configuration=configuration;
		state.changeState(ServerState.SERVER_CONFIGURED);
		controller.start();
	}
	
	/**
	 * Creates the connection with the client. Before call this method the server must be configured through the method <code>configure</code>
	 * @throws ConnectionException If the connection cannot be created.
	 * @throws StateException If the server is not configured.
	 */
	public void createConnection() throws ConnectionException, StateException{
		if(state.getState()==ServerState.SERVER_CONFIGURED){
			state.changeState(ServerState.CLIENT_OFFLINE);
			connection=ServerConnection.getInstance(configuration.getAddress(), configuration.getPort(), configuration.getPassword());
			state.changeState(ServerState.CLIENT_ONLINE);
		}else{
			throw new StateException("configure.first");
		}
	}
	
	/**
	 * Starts the server. The server will starts to receive and handle commands. If the server is already started and the sa
	 * @return <code>true</code> if the server is started, <code>false</code> if is not ready (es. the client is not connected) 
	 */
	public boolean startServer(){
		if(state.getState()==ServerState.CLIENT_ONLINE){
			/*
			 * Request Handling:
			 * -The Handler receive the requests and pass them to the Scheduler
			 * -The scheduler process the requests.
			 */
			scheduler=new SchedulerServer(connection);
			scheduler.start();
			receiver.start();
			return true;
		}else if(state.getState()==ServerState.CLIENT_CONNECTED ){
			receiver.stopHandler();
			receiver=new ServerReceiver();
			receiver.start();
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * This class is responsible for receiving and handle commands. 
	 * @author antessio
	 *
	 */
	private class ServerReceiver extends Thread{
		private boolean running=true;
		@Override
		public void run() {
			while(running){
				if(state.getState()==ServerState.CONNECTION_ALIVE){
					try {
						Request request=(Request)connection.receive();
						try {
							scheduler.scheduleRequest(request);
						} catch (SchedulerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					} catch (ProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ConnectionException e) {
						
					}
				}else{
					try {
						state.wait();
					} catch (InterruptedException e) {
						running=false;
					}
				}
			}
		
		}
		/**
		 * Stops the thread.
		 */
		public void stopHandler(){
			running=false;
			this.interrupt();
		}
		
		/**
		 * @return <code>true</code> if the thread is running, <code>false</code> otherwise.
		 */
		public boolean isRunning(){
			return running;
		}
	}
	
	/**
	 * This class is a thread responsible of the connection monitoring. 
	 * @author antessio
	 *
	 */
	private class ServerController extends Thread{
		public static final int OFFLINE_TIME_MIN=5000;//5 seconds 
		public static final int OFFLINE_TIME_MEDIUM=10000;//10 seconds
		public static final int OFFLINE_TIME_HIGH=20000;//20 seconds
		public static final int OFFLINE_TIME_VERY_HIGH=45000;//45 seconds
		public static final int CLIENT_ONLINE_TIME=2000;//2 seconds
		public static final int CONNECTION_ALIVE_TIME=3000;//3 seconds
		public static final int CLIENT_DISCONNECTED_TIME=5000;//5 seconds
		public static final int CONNECTION_CLOSED_TIME=4000;//4 seconds
		public static final int CLIENT_CONNECTED_TIME=1000;//1 second
		public static final int CLIENT_AUTHENTICATED_TIME=1000;//1 second
		
		private boolean running=true;
		private int attempts=0;
		
		/**
		 * Stops the thread.
		 */
		public void stopController(){
			running=false;
			this.interrupt();
		}
		@Override
		public void run() {
			//checks the state of the connection, sets the variable "state" with the actual state and handles the time for the next check 
			while(running){
				try{
				if(connection.isOnline()){
					if(connection.isOpen()){
						if(connection.isConnected()){
							if(state.getState()==ServerState.CONNECTION_ALIVE){
								state.notifyAll();
							}
							state.changeState(ServerState.CLIENT_CONNECTED);
							Thread.sleep(CLIENT_CONNECTED_TIME);
						}else{
							if(state.getState()==ServerState.CLIENT_CONNECTED){
								//impossible to verify
								state.changeState(ServerState.CONNECTION_CLOSED);
								Thread.sleep(CONNECTION_CLOSED_TIME);
							}else{
								state.changeState(ServerState.CONNECTION_ALIVE);	
								Thread.sleep(CONNECTION_ALIVE_TIME);
							}
						}
						
					}else{
						if(state.getState()==ServerState.CLIENT_CONNECTED){
							//if the previous state was CONNECTED this means that there was a connection shutdown
							state.changeState(ServerState.CLIENT_DISCONNECTED);
							Thread.sleep(CLIENT_DISCONNECTED_TIME);
						}else{
							state.changeState(ServerState.CLIENT_ONLINE);
							Thread.sleep(CLIENT_ONLINE_TIME);
						}
					}
					
					
				}else{
					state.changeState(ServerState.CLIENT_OFFLINE);
					attempts++;
					if(attempts<10){
						Thread.sleep(OFFLINE_TIME_MIN);
					}else if(attempts<50){
						Thread.sleep(OFFLINE_TIME_MEDIUM);
					}else if(attempts<100){
						Thread.sleep(OFFLINE_TIME_HIGH);
					}else{
						Thread.sleep(OFFLINE_TIME_VERY_HIGH);
					}
				}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
}


