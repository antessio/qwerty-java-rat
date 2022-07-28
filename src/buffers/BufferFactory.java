package buffers;

import managers.ProcessType;

/**
 * This class implements a factory method to create a buffer depending on the type of process.
 * @author antessio
 *
 */
public class BufferFactory {

	private static BufferFiles bufferFiles=new BufferFiles();
	private static BufferScreen bufferScreen=new BufferScreen();
	private static BufferCommands bufferCommands=new BufferCommands();
	private static BufferConnection bufferConnection=new BufferConnection();
	
	public static BufferInterface getFactory(ProcessType processType){
		switch(processType.getType()){
			case ProcessType.COMMANDS:
				return bufferCommands;
			case ProcessType.CONNECTION:
				return bufferConnection;
			case ProcessType.FILES:
				return bufferFiles;
			case ProcessType.SCREEN:
				return bufferScreen;
		}
		return null;
	}
}
