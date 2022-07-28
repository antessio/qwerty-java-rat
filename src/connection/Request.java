package connection;

import connection.exceptions.RequestException;
import managers.ProcessType;

/**
 * This class gives the structure of the request, possible requests expressed by constants, and methods to 
 * reads the request.
 * @author antessio
 *
 */
public class Request implements Comparable<Object>{
	

	/*
	 * All possible requests
	 */
	
	public static final String CONNECT = "Connect";
	
	public static final String PING = "ping";
	
	public static final String CLOSE = "Close";
	
	public static final String START_CATCH= "StartCatch";
	
	public static final String FILESYSTEM_ROOTS = "GetFileSystemRoots";
	
	public static final String HOME_DIRECTORY = "GetFileSystemHomeDirectory";
	
	public static final String CHANGE_DIRECTORY = "GetChangeDirectory";

	public static final String DRIVES = "GetFileSystemDrives";

	public static final String LIST_FILES = "GetListFiles";
	
	public static final String DOWNLOAD = "GetDownload";
	
	public static final String UPLOAD = "GetUpload";
	
	public static final String SCREEN = "GetScreen";
	
	public static final String STOP_CATCH = "StopCatch";
	
	public static final String OS = "GetOS";
	
	public static final String OWNER="GetOwner";
	
	public static final String DRIVERS= "GetDrivers";
	
	public static final String RESTART = "Restart";
	
	public static final String SHUTDOWN = "Shutdown";
	
	public static final String OPENED_FILES = "GetOpenedFiles";

	public static final String NET_INTERFACES = "GetNetInterfaces";
	
	public static final String SYSTEM_INFO = "GetSystemInfo";

	public static final String TASK_LIST = "GetTaskList";

	public static final String GENERIC_COMMAND = "Command";

	public static final String COPY_FILE = "GetCopy";

	public static final String MOVES_FILE = "GetMove";

	public static final String RENAME_FILE = "GetRename";

	public static final String PARAM_SEPARATOR = "&";
	
	public static final String MSG_SEPARATOR="#";
	
	public static final String[] CONNECTION={CONNECT,PING,CLOSE};
	public static final String[] COMMANDS={DRIVERS,GENERIC_COMMAND,NET_INTERFACES,OPENED_FILES,OS,OWNER,RESTART,SHUTDOWN,SYSTEM_INFO,TASK_LIST};
	public static final String[] FILE_SYSTEM={CHANGE_DIRECTORY,COPY_FILE,DOWNLOAD,DRIVES,FILESYSTEM_ROOTS,HOME_DIRECTORY,LIST_FILES,MOVES_FILE,RENAME_FILE,UPLOAD};
	public static final String[] SCREENS={SCREEN,START_CATCH,STOP_CATCH};
	private ProcessType processType;
	private String command;
	private String [] parameters;
	private int sender;
	private int idRequest;
	
	
	public Request(ProcessType processType, String command,
			String[] parameters, int sender, int idRequest) throws RequestException{
		super();
		if(processType==null || command==null )
			throw new RequestException("invalid.parameters");
		this.processType = processType;
		this.parameters = parameters;
		this.sender = sender;
		this.idRequest = idRequest;
		switch(processType.getType()){
			case ProcessType.COMMANDS:
				if(!(command.equals(DRIVES) || command.equals(HOME_DIRECTORY) || 
						command.equals(RESTART) || command.equals(OWNER) || command.equals(SHUTDOWN) || 
						command.equals(OPENED_FILES) || command.equals(NET_INTERFACES) || command.equals(SYSTEM_INFO) ||
						command.equals(TASK_LIST) || command.equals(GENERIC_COMMAND) || command.equals(OS) ||
						command.equals(COPY_FILE) || command.equals(MOVES_FILE) || command.equals(RENAME_FILE)))
					throw new RequestException("command.not.valid.for.process");
				
				break;
			case ProcessType.CONNECTION:
				if(!(command.equals(CONNECT)|| command.equals(PING) || command.equals(CLOSE)))
					throw new RequestException("command.not.valid.for.process");
				break;
				
			case ProcessType.FILES:
				if(!(command.equals(FILESYSTEM_ROOTS) || command.equals(LIST_FILES) || command.equals(DOWNLOAD) )
						|| command.equals(UPLOAD) || command.equals(DRIVES) || command.equals(CHANGE_DIRECTORY))
					throw new RequestException("command.not.valid.for.process");
				break;
			case ProcessType.SCREEN:
				if(!(command.equals(SCREEN) || command.equals(START_CATCH) || command.equals(STOP_CATCH)) )
					throw new RequestException("command.not.valid.for.process");
				break;
				
			default: 
				throw new RequestException("wrong.process");
		}
		this.command = command;
	}
	public ProcessType getProcessType() {
		return processType;
	}
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String[] getParameters() {
		return parameters;
	}
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public int getIdRequest() {
		return idRequest;
	}
	public void setIdRequest(int idRequest) {
		this.idRequest = idRequest;
	}
	
	/**
	 * This method create a Request object from a received message.
	 * @param msg Received Message.
	 * @return Request
	 * @throws RequestException 
	 */
	public static Request createRequestFromString(String msg) throws RequestException{
		Request request=null;
		//request format string: id#sender#processType#command#parameters
		String [] split=msg.split(MSG_SEPARATOR);
		int id=Integer.parseInt(split[0]);
		int sender=Integer.parseInt(split[1]);
		String proc=split[2];
		ProcessType processType=new ProcessType(ProcessType.nameToType(proc.split(ProcessType.SEPARATOR)[0]));
//		processType.setName(split[2]);
//		processType.setType(ProcessType.nameToType(split[2]));
		
		String command=split[3];
		String [] parameters;
		if(split.length>4)
			parameters=split[4].split(PARAM_SEPARATOR);
		else parameters=new String[0];
		request=new Request(processType, command, parameters, sender, id);
		return request;
	}
	
	
	@Override
	public int compareTo(Object o) {
		Request r=(Request)o;
		if(r.getIdRequest()==idRequest)
			return 0;
		return -1;
	}
	
	private boolean equalsParameters(String [] params){
		if(params==null && parameters==null)
			return true;
		else if( (params==null && parameters!=null) || (params!=null && parameters==null) )
			return false;
		if(params.length!=parameters.length)
			return false;
		for(int i=0; i<params.length; i++){
			if(!params[i].equals(parameters[i]))
				return false;
		}
		return true;
		
	}
	@Override
	public boolean equals(Object obj) {
		Request toCompare=(Request)obj;
		if(toCompare.getIdRequest()==idRequest && toCompare.getProcessType().equals(processType) 
				&& toCompare.getCommand().equals(command) && equalsParameters(parameters) && 
				toCompare.getSender()==sender)
			return true;
		else return false;
		
	}
	@Override
	public String toString() {
		String toString="";
		toString=idRequest+MSG_SEPARATOR+sender+MSG_SEPARATOR+processType+MSG_SEPARATOR+command+MSG_SEPARATOR;
		for(int i=0; i<parameters.length; i++)
			toString+=parameters[i]+PARAM_SEPARATOR;
		if(parameters.length>0)
			toString=toString.substring(0, toString.length()-1);
		return toString;
	}
	
}
