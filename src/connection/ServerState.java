package connection;

/**
 * This class represents all possible states of the server.
 * @author antessio
 *
 */
public class ServerState extends connection.AbstractState {
	
	//when client is connected, but not authenticated
	public static final int CLIENT_CONNECTED=200;
	
	//when client closes connection 
	public static final int CONNECTION_CLOSED=202;
	
	//when the client become off line
	public static final int CLIENT_DISCONNECTED=201;
	
	//when the client is online but not connected
	public static final int CONNECTION_ALIVE=203;
	
	//when client is off line and not connected
	public static final int CLIENT_OFFLINE=204;
	
	//when the client is online but is not connected and not authenticated
	public static final int CLIENT_ONLINE=205;
	
	//when the server is configured and ready to try connection
	public static final int SERVER_CONFIGURED=206;
	
	//first state
	public static final int OFFLINE=207;
	
	public ServerState(int state) {
		super(state);
	}

	@Override
	public String getStateName() {
		 String s="";
	        
	        if(state==200){
	            s="Connected";
	        }
	        if(state==202){
	            s="Connection Closed";
	        }
	        if(state==201){
	            s="Client disconnected";
	        }
	        if(state==203){
	        	s="Connection alive";
	        }
	        if(state==204){
	        	s="Client off line";
	        }
	        if(state==205){
	        	s="Client Online";
	        }
	        if(state==206){
	        	s="Server Configured";
	        }
	        if(state==207){
	        	s="Server Offline";
	        }
	        return s;
	}

}
