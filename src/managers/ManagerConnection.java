package managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import managers.events.ConnectionEvent;
import managers.exceptions.ManagerException;
import managers.listeners.ManagerConnectionEventListener;
import connection.Request;
import connection.ServerInfo;
import connection.ConnectionInterface;
import connection.QwertyConnection;
import connection.Response;
import connection.ClientState;
import connection.exceptions.ConnectionException;
import connection.receiver.Receiver;
import connection.scheduler.Scheduler;

public class ManagerConnection implements Manager, Runnable{

	private static ManagerConnection instance;
	private ConnectionInterface connection;
	private int port;
	private List<ServerInfo> connectionList;
	private String error;
	private boolean isError;
	private boolean connectionCreated;
	private Controller controller;
	List<ManagerConnectionEventListener> listeners;
	private boolean configured=false;
	
	public static ManagerConnection getInstance(){
		if(instance==null)
			instance=new ManagerConnection();
		return instance;
	}
	
	public void resetManager(){
		instance=null;
		if(controller!=null){
			controller.stopRunning();
		}
		configured=false;
		port=0;
		listeners=null;
		connectionList=null;
		
	}
	private ManagerConnection(){
		listeners=new ArrayList<ManagerConnectionEventListener>();
		
	}
	
	public void configure(int port){
		configured=true;
		this.port=port;
	}
	
	public List<ServerInfo> getConnections() throws ManagerException{
		if(!configured)
			throw new ManagerException("configure.first");
		connectionList=new ArrayList<ServerInfo>();
		new Thread(this).start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connectionList;
	}
	
	@Override
	public Response handle(String command, String[] parameters) throws ManagerException {
		Response response=new Response();
		response.setProcessType(new ProcessType(ProcessType.CONNECTION));
		response.setResponseId(-1);
		response.setSender(-1);
		try {
//			Request request = new Request(new ProcessType(ProcessType.CONNECTION), command, parameters, -1, -1);
			if(connectionCreated){
				if(command.equals(Request.CONNECT)){
					if(connection.isOpen()){
						if(connection.connect()){
							response.setReceived(Response.OK);
							try{
								controller=new Controller(connection,listeners);
								controller.start();
							}catch(Exception e){
								throw new ManagerException("connection.cannot.be.null");
							}
						}else{
							response.setReceived(Response.NOK);
						}
					}else{
						response.setReceived(Response.NOK);
					}
				}
			if(command.equals(Request.CLOSE)){
				controller.stopRunning();
				connection.close();
				if(!connection.isOpen())
					response.setReceived(Response.OK);
				else response.setReceived(Response.NOK);
			}
			
		}else{
			response.setReceived(Response.NOK);
		}
		} catch (ConnectionException e) {
			throw new ManagerException(e.getMessage(), e);
//		}catch(RequestException e){
//			throw new ManagerException(e.getMessage(), e);
		}
		return response;
	}

	@Override
	public void addEventListener(EventListener listener) {
		listeners.add((ManagerConnectionEventListener) listener);
	}

	@Override
	public void run() {
		ServerSocket client = null;
		if(configured){
			try {
				client = new ServerSocket(port);
				client.setSoTimeout(1000);
				boolean flag=true;
				while(flag){
					Socket received=client.accept();
					if(received!=null){
						ServerInfo s = new ServerInfo(received, received.getPort(), received.getInetAddress().toString(), received.getInetAddress().getHostName());
						for(Iterator<ManagerConnectionEventListener> it=listeners.iterator();it.hasNext();){
							ManagerConnectionEventListener mcel=(ManagerConnectionEventListener)it.next();
							mcel.connectionReceived(new ConnectionEvent("New Connection Received", 0, s));
						}
						connectionList.add(s);
						
					}else{
						flag=false;
					}
					
				}
			}catch(IOException e){
				error=e.getMessage();
				isError=true;
			}finally{
				if(client!=null){
					try {
						client.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			error="configure.first";
			isError=true;
		}
		
	}
	
	/**
	 * Creates the connection with the selected server.
	 * @param n Id of server in list.
	 * @param password The password to login 
	 * @throws ManagerException when the id is invalid
	 * @return <c>true</c> if created, <c>false</c>otherwise.
	 */
	public boolean createConnection(int id,String password)throws ManagerException{
		if(!configured)
			throw new ManagerException("configure.first");
		if(password==null)
			password="";
		ServerInfo serverInfo=connectionList.get(id);
		if(serverInfo==null)
			throw new ManagerException("invalid.server.id");
		if(serverInfo.getSocket().isClosed())
			throw new ManagerException("selected.connection.was.closed");
		connection=QwertyConnection.getInstance(serverInfo.getSocket(), password);
		if(connection!=null){
			connection.open();
			connectionCreated=connection.isOpen();
			if(connectionCreated){
				for(Iterator<ManagerConnectionEventListener> it=listeners.iterator();it.hasNext();){
					ManagerConnectionEventListener mcel=(ManagerConnectionEventListener)it.next();
					mcel.newConnection(new ConnectionEvent("Connection Created", 0));
				}
			}
		}else connectionCreated=false;
		
		return connectionCreated;
	}

}

class Controller extends Thread{
	
	private ConnectionInterface connection;
	private List<ManagerConnectionEventListener> listeners;
	final int TIME_NOT_CONNECTED=2000;
	final int TIME_CONNECTED=5000;
	final int MAX_TIME_ELAPSED=6000;
	private boolean running=true;
	
	public Controller(ConnectionInterface connection,List<ManagerConnectionEventListener> listeners) throws Exception{
		if(connection==null)
			throw new Exception("connection.cannot.be.null"); 
		this.connection=connection;
		this.listeners=listeners;
	}
	
	public void stopRunning(){
		running=false;
	}
	@Override
	public void run(){
			
			while(running){
				try {
						if(connection.isConnected()){
							if(((QwertyConnection)connection).ping()){
								System.out.println("Controller: Connected"); //test
								Thread.sleep(TIME_CONNECTED);
							}else{
								System.out.println("Controller: CONNECTION FAULT"); //test
								throw new ConnectionException("connection.fault");
							}

						}else {
							//not connected 
							System.out.println("Controller: NOT Connected"); //test
							Thread.sleep(TIME_NOT_CONNECTED);
						}
					
				} catch (ConnectionException e) {
					for(Iterator<ManagerConnectionEventListener> it=listeners.iterator();it.hasNext();){
						ManagerConnectionEventListener mcel=(ManagerConnectionEventListener)it.next();
						mcel.connectionFault(new ConnectionEvent("Connection unexpectelly closed", 0));
					}
					running=false;
				}catch(InterruptedException e){
					running=false;
				}
			}
		
	}
}