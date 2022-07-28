package test.connection;

import static org.junit.Assert.*;

import managers.ProcessType;

import org.junit.Test;

import connection.Request;
import connection.exceptions.RequestException;

public class TestRequest {

	@Test
	public void testCompare() {
		ProcessType processType=new ProcessType(ProcessType.COMMANDS, ProcessType.typeToName(ProcessType.COMMANDS));
		try{
			Request requestTest1=new Request(processType, Request.GENERIC_COMMAND, new String[]{"dir"}, 1, 1);
			Request requestTest2=new Request(processType, Request.GENERIC_COMMAND, new String[]{"dir"}, 1, 2);
			assertEquals(requestTest1.compareTo(requestTest2),-1);
		}catch(RequestException ex){
			fail();
		}
	}
	
	@Test
	public void testCreateRequest(){
		ProcessType processType=new ProcessType(ProcessType.COMMANDS, ProcessType.typeToName(ProcessType.COMMANDS));
		Request expected;
		try {
			expected = new Request(processType, Request.GENERIC_COMMAND, new String[]{"dir"}, 1, 1);
			String msg="1#1#COMMANDS#Command#dir";
			Request actual=Request.createRequestFromString(msg);
			assertEquals(expected, actual);
		} catch (RequestException e) {
			fail();
		}
	}
	
	@Test
	/**
	 * Create request wrong: command not reconized for process type
	 */
	public void testCreateRequest2(){
		ProcessType processType=new ProcessType(ProcessType.COMMANDS);
		Request expected;
		try {
			expected = new Request(processType, Request.SCREEN, new String[]{"dir"}, 1, 1);
			String msg="1#1#COMMANDS#Command#dir";
			Request actual=Request.createRequestFromString(msg);
			fail();
		} catch (RequestException e) {
			assertEquals("command.not.valid.for.process",e.getMessage());
		}
	}

}
