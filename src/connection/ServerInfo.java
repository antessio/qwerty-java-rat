package connection;

import java.net.Socket;

/**
 * This class joins all info about the server.
 * @author antessio
 *
 */
public class ServerInfo {
	
	private Socket socket;
	private String osName;
	private String address;
	private int port;
	private String owner;
	
	/**
	 * Default constructor.
	 */
	public ServerInfo(){
		
		
	}
	@Override
	public String toString() {
		String s="";
		if(!owner.equals(""))
			s+="Owner: "+owner+"\n";
		if(!osName.equals("")){
			s+="OS: "+osName+"\n";
		}
		s+="Address: "+address+" Port: "+port+"\n";
		return s;
	}
	
	/**
	 * Constructor. 
	 * @param socket Received socket.
	 * @param port Communication port.
	 * @param address Server Address.
	 * @param owner Server owner.
	 */
	    public ServerInfo(Socket socket, int port, String address,String owner) {
	        this.socket=socket;
	        this.port = port;
	        this.address = address;
	        this.owner=owner;
	        this.osName="";
	    }
	
	
	/**
	 * Returns the operating system name.
	 * @return
	 */
	public String getOsName() {
		return osName;
	}
	/**
	 * Sets the operating system name.
	 * @param osName
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}
	
	/**
	 * Returns the address.
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * Sets the address.
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * Returns the port.
	 * @return
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets the port
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * Returns the owner.
	 * @return
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * Sets the owner.
	 * @param owner
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * Returns the socket received.
	 * @return Socket Received
	 */
	public Socket getSocket(){
		return socket;
	}
	

}
