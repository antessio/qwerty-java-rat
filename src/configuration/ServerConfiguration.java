package configuration;

/**
 * This class keeps the parameters for server configuration. 
 * @author antessio
 *
 */
public class ServerConfiguration {

	private String address;
	private int port;
	private String password;
	
	/**
	 * Constructor
	 * @param address Client Address.
	 * @param port Client port.
	 * @param password Password for authentication.
	 */
	public ServerConfiguration(String address, int port, String password) {
		this.address = address;
		this.port = port;
		this.password = password;
	}

	
	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public String getPassword() {
		return password;
	}
	
	
	
}
