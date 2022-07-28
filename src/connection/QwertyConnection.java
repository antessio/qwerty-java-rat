package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import managers.ProcessType;

import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;
import connection.exceptions.RequestException;

public class QwertyConnection implements ConnectionInterface{

	private Socket server;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private boolean isConnected;
	private boolean isOpen;
	private boolean isBusy;
	private String password;
	private ServerInfo serverInfo;
	private static QwertyConnection instance;
	
	public static void resetConnection(){
		instance=null;
	}
	
	public static QwertyConnection getInstance(Socket received, String password){
		if(instance==null)
			instance=new QwertyConnection(received, password);
		return instance;
	}
	
	public static QwertyConnection getInstance(Socket received){
		if(instance==null)
			instance=new QwertyConnection(received);
		return instance;
	}
	private QwertyConnection(Socket received, String password){
		server=received;
		this.password=password;
	}
	
	private QwertyConnection(Socket received){
		server=received;
	}
	
	@Override
	/**
	 * See interface. 
	 */
	public void open() {
		try {
			os=new ObjectOutputStream(server.getOutputStream());
			is=new ObjectInputStream(server.getInputStream());
			isOpen=true;
		} catch (IOException e) {
			isOpen=false;
		}
	}

	@Override
	public boolean isOpen() {
		if(server.isClosed() || os==null || is==null)
			isOpen=false;
		return isOpen;
	}

	@Override
	/**
	 * Closes the connection.  
	 * @throws ConnectionException If the server was not informed about closing (send(CLOSE)).
	 */
	public void close() throws ConnectionException {
		try {
			String [] parameters=new String[0];
			try {
				Request request;
					request = new Request(new ProcessType(ProcessType.CONNECTION), Request.CLOSE,parameters , 0, 0);
				
				send(request);
			}catch(ConnectionException e){
				
			}catch(ProtocolException e){
				
			} catch (RequestException e) {
			}
			os.close();
			is.close();
			server.close();
			isOpen=false;
			isConnected=false;
		} catch (IOException e) {
			throw new ConnectionException("io.exception",e);
		}
	}

	@Override
	/**
	 * This method creates the real connection. 
	 * @return <c>true</c> if connection is done, <c>false</c> otherwise
	 * @throws ConnectionException When the connection is not open (msg: "connection.must.be.open")\n
	 * 							   When the client is already connected (msg: "already.connected")\n
	 * 							   When catch a request exception or a protocol exception.
	 */
	public boolean connect() throws ConnectionException {
		if(!isOpen)
			throw new ConnectionException("connection.must.be.open");
		if(isConnected)
			throw new ConnectionException("already.connected");
		String [] parameters=new String[1];
		parameters[0]=password;
		try {
			Request request=new Request(new ProcessType(ProcessType.CONNECTION), Request.CONNECT,parameters , 0, 0);
			send(request);
			Response response=(Response)is.readObject();
			if(response.getReceived().equals(Response.OK)){
				isConnected=true;
				return isConnected;
			}
			else{
				isConnected=false;
				return isConnected;
			}
		} catch (RequestException e) {
			throw new ConnectionException(e.getMessage(),e);
		}catch(ProtocolException e){
			throw new ConnectionException(e.getMessage(),e);
		}catch(ClassNotFoundException e){
			throw new ConnectionException(e.getMessage(),e);
		}catch(IOException e) {
			throw new ConnectionException("io.exception",e);
		}
		
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	/**
	 * Sends an object through connection.  
	 * @param obj The object to send
	 * @throws ProtocolException Is unnecessary 
	 * @throws ConnectionException When connection is not open \n
	 * 							   When connection is not created by connect \n
	 * 							   When an IOException occurs.
	 */
	public  void send(Object obj) throws ProtocolException, ConnectionException {
		Request request = (Request)obj;
		if(!isOpen)
			throw new ConnectionException("connection.must.be.open");
		if(!isConnected && !request.getCommand().equals(Request.CONNECT) )
			throw new ConnectionException("connect.first");
		
		try {
			os.writeObject(request.toString());
			os.flush();
		} catch (IOException e) {
			throw new ConnectionException("io.exception",e);
		}
	}

	
	@Override
	/**
	 * Receives an object from connection.
	 * @return The response received.
	 * @throws ProtocolException Is unnecessary
	 * @throws ConnectionException When connection is not open \n
	 * 							   When connection is not created by connect \n
	 * 							   When an IOException occurs.
	 */
	public synchronized Object receive() throws ProtocolException, ConnectionException {
		Response response=null;
		if(!isOpen)
			throw new ConnectionException("connection.must.be.open");
		if(!isConnected)
			throw new ConnectionException("connect.first");
		try {
			response=(Response)is.readObject();
		} catch (ClassNotFoundException e) {
			throw new ConnectionException(e.getMessage(),e);
		} catch (IOException e) {
			throw new ConnectionException("io.exception",e);
		}
		return response;
		
	}
	
	/**
	 * Checks if the server connected by this connection is active.
	 * @return <c>true</c> if active, <c>false</c> otherwise.
	 * @throws ConnectionException If there are connection faults.
	 */
	public synchronized boolean  ping() throws ConnectionException{
		int id=RequestIdGenerator.getInstance().getNextId();
		try {
			String[] parameters=new String[0];
			Request request=new Request(new ProcessType(ProcessType.CONNECTION),Request.PING, parameters,id,id );
			try {
				send(request);
				Response response=(Response)receive();
				if(response.getReceived().equals(Response.PONG))
					return true;
				else return false;
			} catch (ProtocolException e) {
				throw new ConnectionException(e.getMessage(), e);
			}
			
		}catch(RequestException e){
			throw new ConnectionException(e.getMessage(),e);
		}
	}

}
