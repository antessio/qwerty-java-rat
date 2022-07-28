package test.buffers;

import static org.junit.Assert.*;

import managers.ProcessType;

import org.junit.Test;

import connection.Request;
import connection.Response;
import connection.exceptions.RequestException;

import buffers.BufferCommands;
import buffers.BufferFactory;
import buffers.exceptions.BufferException;

public class TestBufferCommands {
	BufferCommands bufferCommands = (BufferCommands) BufferFactory
			.getFactory(new ProcessType(ProcessType.COMMANDS, ProcessType
					.typeToName(ProcessType.COMMANDS)));

	@Test
	/**
	 * Add correct
	 */
	public void testAdd1() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.COMMANDS,
				ProcessType.typeToName(ProcessType.COMMANDS));
		Request request = new Request(processType, Request.GENERIC_COMMAND,
				new String[] { "dir" }, 1, 1);
		try {
			bufferCommands.add(request);
		} catch (BufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	/**
	 * Add wrong: is trying to add a request not consistent
	 */
	public void testAdd2() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.CONNECTION);
		Request request = new Request(processType, Request.CONNECT,
				new String[] { "dir" }, 1, 2);
		try {
			bufferCommands.add(request);
			fail();
		} catch (BufferException e) {
			assertEquals("request.processType.not.consistent", e.getMessage());

		}
	}

	@Test
	/**
	 * Add response to the request correct
	 */
	public void testAdd3() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.COMMANDS,
				ProcessType.typeToName(ProcessType.COMMANDS));
		Request request = new Request(processType, Request.GENERIC_COMMAND,
				new String[] { "dir" }, 1, 3);
		Response response = new Response(processType, "file1.txt\nfile2.txt",
				1, 3);
		try {
			bufferCommands.add(request);
			bufferCommands.add(request, response);
		} catch (BufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	/**
	 * Add response to the request wrong: is trying to add a not consistent request
	 */
	public void testAdd4() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.CONNECTION);
		Request request = new Request(processType, Request.CONNECT,
				new String[] { "dir" }, 1, 4);
		Response response = new Response(processType, "file1.txt\nfile2.txt",
				1, 4);
		try {
			bufferCommands.add(request);
			bufferCommands.add(request, response);
			fail();
		} catch (BufferException e) {
			assertEquals("request.processType.not.consistent", e.getMessage());

		}

	}

	@Test
	/**
	 * Add response to the request wrong: is trying to add a not consistent response
	 */
	public void testAdd5() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.COMMANDS);
		Request request = new Request(processType, Request.GENERIC_COMMAND,
				new String[] { "dir" }, 1, 5);
		Response response = new Response(new ProcessType(ProcessType.CONNECTION), "Connected",
				1, 5);
		try {
			bufferCommands.add(request);
			bufferCommands.add(request, response);
			fail();
		} catch (BufferException e) {
			assertEquals("response.processType.not.consistent", e.getMessage());

		}

	}
	@Test
	/**
	 * Add response to the request wrong: is trying to add a response not mapped with an existent request
	 */
	public void testAdd6() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.COMMANDS);
		Request request = new Request(processType, Request.GENERIC_COMMAND,
				new String[] { "dir" }, 1, 123);
		Response response = new Response(processType, "Connected",
				1, 123);
		try {
			bufferCommands.add(request, response);
			fail();
		} catch (BufferException e) {
			assertEquals("no.request", e.getMessage());

		}

	}
	
	@Test
	/**
	 * Get Response correct
	 */
	public void testGet1() throws RequestException{
		ProcessType processType = new ProcessType(ProcessType.COMMANDS);
		Request request = new Request(processType, Request.GENERIC_COMMAND,
				new String[] { "dir" }, 1, 100);
		Response response = new Response(processType, "file1.txt",
				1, 100);
		try {
			bufferCommands.add(request);
			bufferCommands.add(request, response);
			assertEquals(response, bufferCommands.get(request));
		} catch (BufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	/**
	 * Get Response correct: no response associated
	 */
	public void testGet2() throws RequestException{
		ProcessType processType = new ProcessType(ProcessType.COMMANDS);
		Request request = new Request(processType, Request.GENERIC_COMMAND,
				new String[] { "dir" }, 1, 200);
//		Response response = new Response(processType, "Connected",
//				1, 200);
		try {
			bufferCommands.add(request);
			//bufferCommands.add(request, response);
			assertNull(bufferCommands.get(request));
			
		} catch (BufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	/**
	 * Get Response wrong: response not consistent
	 */
	public void testGet3() throws RequestException{
		ProcessType processType = new ProcessType(ProcessType.COMMANDS);
		Request request = new Request(processType, Request.GENERIC_COMMAND,
				new String[] { "dir" }, 1, 240);
		Response response = new Response(processType, "Connected",
				1, 200);
		try {
			bufferCommands.add(request);
			request.setProcessType(new ProcessType(ProcessType.CONNECTION));
			response=bufferCommands.get(request);
			fail();
		} catch (BufferException e) {
			assertEquals("request.processType.not.consistent", e.getMessage());
		}

	}
	
	@Test
	public void testGetNext(){
		bufferCommands=new BufferCommands();
		ProcessType processType = new ProcessType(ProcessType.COMMANDS);
		Request request1,request2,request3,request4;
		try {
				request1 = new Request(processType, Request.GENERIC_COMMAND,
					new String[] { "dir" }, 1, 240);
				request2 = new Request(processType, Request.GENERIC_COMMAND,
						new String[] { "cd" }, 1, 241);
				request3 = new Request(processType, Request.GENERIC_COMMAND,
						new String[] { "netinterface" }, 1, 242);
				request4 = new Request(processType, Request.GENERIC_COMMAND,
						new String[] { "dir" }, 1, 243);
			try {
				bufferCommands.add(request1);
				bufferCommands.add(request3);
				bufferCommands.add(request2);
				bufferCommands.add(request4);
				assertEquals(request1,bufferCommands.getNext());
				assertEquals(request3,bufferCommands.getNext());
				assertEquals(request2,bufferCommands.getNext());
				assertEquals(request4,bufferCommands.getNext());
			} catch (BufferException e) {
				e.printStackTrace();
				fail();
			}
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testFindRequest(){
		bufferCommands=new BufferCommands();
		ProcessType processType = new ProcessType(ProcessType.COMMANDS);
		Request request1,request2,request3,request4;
		try {
				request1 = new Request(processType, Request.GENERIC_COMMAND,
					new String[] { "dir" }, 1, 240);
				request2 = new Request(processType, Request.GENERIC_COMMAND,
						new String[] { "cd" }, 1, 241);
				request3 = new Request(processType, Request.GENERIC_COMMAND,
						new String[] { "netinterface" }, 1, 242);
				request4 = new Request(processType, Request.GENERIC_COMMAND,
						new String[] { "dir" }, 1, 243);
			try {
				bufferCommands.add(request1);
				bufferCommands.add(request3);
				bufferCommands.add(request2);
				bufferCommands.add(request4);
				assertEquals(request1, bufferCommands.findRequestById(240));
				assertEquals(request4, bufferCommands.findRequestById(243));
				assertEquals(request2, bufferCommands.findRequestById(241));
				assertEquals(request3, bufferCommands.findRequestById(242));
				
			} catch (BufferException e) {
				e.printStackTrace();
				fail();
			}
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
