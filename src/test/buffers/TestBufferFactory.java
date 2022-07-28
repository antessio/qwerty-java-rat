package test.buffers;

import static org.junit.Assert.*;

import managers.ProcessType;

import org.junit.Test;

import buffers.BufferCommands;
import buffers.BufferFactory;
import buffers.BufferFiles;
import buffers.BufferInterface;
import buffers.BufferScreen;

public class TestBufferFactory {

	@Test
	public void testGetFactory() {
		ProcessType processType=new ProcessType(ProcessType.COMMANDS, ProcessType.typeToName(ProcessType.COMMANDS));
		BufferInterface bufferCommands=BufferFactory.getFactory(processType);
		if(!(bufferCommands instanceof BufferCommands))
			fail();
		processType=new ProcessType(ProcessType.FILES, ProcessType.typeToName(ProcessType.FILES));
		BufferInterface bufferFiles=BufferFactory.getFactory(processType);
		if(!(bufferFiles instanceof BufferFiles))
			fail();
		processType=new ProcessType(ProcessType.SCREEN, ProcessType.typeToName(ProcessType.SCREEN));
		BufferInterface bufferScreen=BufferFactory.getFactory(processType);
		if(!(bufferScreen instanceof BufferScreen))
			fail();
		
		
	}

}
