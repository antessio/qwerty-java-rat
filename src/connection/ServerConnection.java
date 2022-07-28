package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import managers.ProcessType;
import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;
import connection.exceptions.RequestException;

/**
 * This class implements the connection used by the server. Through this class, the server can
 * be connected to the client and send/receive message to/from the client. 
 * @author antessio
 */
public class ServerConnection implements ConnectionInterface{

	private Socket socket;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private boolean isConnected;
	private boolean isOpen;
	private boolean isBusy;
	private String password;
	private static ServerConnection instance;
	private String address;
	private int port;
	private boolean isOnline=false;
	
	/**
	 * Resets the instance of connection.
	 */
	public static void resetConnection(){
		instance=null;
	}
	/**
	 * Singleton method used to maintain a single instance of the connection.
	 * @param address Client Address.
	 * @param port Client Port.
	 * @param password Password to authenticate. 
	 * @return The single instance of the connection.
	 * @throws ConnectionException If the connection cannot be created. 
	 */
	public static ServerConnection getInstance(String address, int port,String password) throws ConnectionException{
		if(instance==null){
			try{
				instance=new ServerConnection(address,port,password);
			}catch(IOException e){
				throw new ConnectionException(e.getMessage(),e);
			}
		}
		return instance;
	}
	
	/**
	 * Constructor.
	 * @param address Client Address.
	 * @param port Client Port.
	 * @param password Password to authenticate.
	 * @throws IOException If the socket cannot be created.
	 */
	private ServerConnection(String address, int port,String password) throws IOException{
		this.address=address;
		this.port=port;
		this.socket=new Socket(address,port);
		this.password=password;
		isOnline=true;
	}
	
	@Override
	/**
	 * @see ConnectionInterface#open()
	 */
	public void open() {
		try {
			os=new ObjectOutputStream(socket.getOutputStream());
			is=new ObjectInputStream(socket.getInputStream());
			isOpen=true;
		} catch (IOException e) {
			isOpen=false;
		}
	}

	@Override
	/**
	 * @see ConnectionInterface#isOpen()
	 */
	public boolean isOpen() {
		if(os==null || is==null)
			isOpen=false;
		else
			isOpen=true;
		return isOpen;
	}

	@Override
	/**
	 * @see ConnectionInterface#close()
	 */
	public void close() throws ConnectionException {
		if(isConnected || isOpen)
			throw new ConnectionException("connection.cannot.be.closed");
		try {
			os.close();
			is.close();
			socket.close();
		} catch (IOException e) {
			throw new ConnectionException("io.exception",e);
		}
	}

	@Override
	/**
	 * @see ConnectionInterface#connect()
	 */
	public boolean connect() throws ConnectionException {
		if(!isOpen)
			throw new ConnectionException("connection.must.be.open");
		if(isConnected)
			throw new ConnectionException("already.connected");
		String [] parameters=new String[1];
		parameters[0]=password;
		try {
			String msg= (String)is.readObject();
			Request request = Request.createRequestFromString(msg);
			if(request.getProcessType().getType()==ProcessType.CONNECTION && request.getCommand().equals(Request.CONNECT)){
				Response response=new Response();
				response.setProcessType(request.getProcessType());
				response.setResponseId(request.getIdRequest());
				response.setSender(request.getSender());
				
				if(request.getParameters()[0].equals(password)){
					isConnected=true;
					response.setReceived(Response.OK);
				}else{
					isConnected=false;
					response.setReceived(Response.NOK);
				}
				os.writeObject(response);
				os.flush();
			}
//			Request request=new Request(new ProcessType(ProcessType.CONNECTION), Request.CONNECT,parameters , 0, 0);
//			
//			
//			send(request);
//			Response response=(Response)is.readObject();
//			if(response.getReceived().equals(Response.OK)){
//				isConnected=true;
//				return isConnected;
//			}
//			else{
//				isConnected=false;
//				return isConnected;
//			}
		} catch (RequestException e) {
			throw new ConnectionException(e.getMessage(),e);
		}catch(ClassNotFoundException e){
			throw new ConnectionException(e.getMessage(),e);
		}catch(IOException e) {
			throw new ConnectionException("io.exception",e);
		}
		return isConnected;
		
	}

	/**
	 * Set the flag <code>isConneced</code> of the server that gives information about the state.
	 * @param isConnected If <code>true</code> the server is connected to the client, <code>false</code> otherwise.
	 */
	public void connected(boolean isConnected){
		this.isConnected=isConnected;
	}
	
	@Override
	/**
	 * @see ConnectionInterface#isConnected()
	 */
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	/**
	 * @see ConnectionInterface#send(Object obj)
	 */
	public void send(Object obj) throws ProtocolException, ConnectionException {
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
	 * @see ConnectionInterface#receive()
	 */
	public Object receive() throws ProtocolException, ConnectionException {
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
	 * Returns <code>true</code> if the client is online, <code>false</code> otherwise.
	 * @return <code>true</code> if the client is online, <code>false</code> otherwise.
	 */
	public boolean isOnline(){
		if(socket!=null)
			return true;
		else return false; 
	}
	

}
	