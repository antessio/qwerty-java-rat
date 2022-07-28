package connection;

public class ClientState extends AbstractState{
	
	public static final int NOT_CONNECTED=100;
	
	public static final int CONNECTED=102;
	
	public static final int WAITING_CONNECTIONS=101;

	
	public ClientState(int state) {
		super(state);
	}

	@Override
	public  String getStateName() {
		 String s="";
	        
	        if(state==100){
	            s="Not Connected";
	        }
	        if(state==102){
	            s="Client connected to a server";
	        }
	        if(state==101){
	            s="Client started to receive connection";
	        }
	        
	        return s;
	}

}
