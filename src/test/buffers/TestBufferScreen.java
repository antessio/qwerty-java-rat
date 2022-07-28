package test.buffers;

import static org.junit.Assert.*;

import managers.ProcessType;

import org.junit.Test;

import buffers.BufferCommands;
import buffers.BufferFactory;
import buffers.BufferFiles;
import buffers.BufferScreen;
import buffers.exceptions.BufferException;
import connection.Request;
import connection.Response;
import connection.exceptions.RequestException;

public class TestBufferScreen {

	BufferScreen bufferScreen = (BufferScreen) BufferFactory
			.getFactory(new ProcessType(ProcessType.SCREEN));

	@Test
	/**
	 * Add correct
	 */
	public void testAdd1() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request = new Request(processType, Request.START_CATCH,
				new String[] { "120","120" }, 1, 1);
		try {
			bufferScreen.add(request);
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
				new String[] { "120","120" }, 1, 2);
		try {
			bufferScreen.add(request);
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
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request = new Request(processType, Request.START_CATCH,
				new String[] { "120","120" }, 1, 3);
		int[] rgb={200,300,300};
		Response response = new Response(processType, rgb,
				1, 3);
		try {
			bufferScreen.add(request);
			bufferScreen.add(request, response);
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
				new String[] { "121","120" }, 1, 4);
		int[] rgb={200,300,300};
		Response response = new Response(processType, rgb,
				1, 4);
		try {
			bufferScreen.add(request);
			bufferScreen.add(request, response);
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
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request = new Request(processType, Request.SCREEN,
				new String[] { "300","300" }, 1, 5);
		Response response = new Response(new ProcessType(ProcessType.CONNECTION), "Connected",
				1, 5);
		try {
			bufferScreen.add(request);
			bufferScreen.add(request, response);
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
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request = new Request(processType, Request.SCREEN,
				new String[] { "120","300" }, 1, 123);
		Response response = new Response(processType, "Connected",
				1, 123);
		try {
			bufferScreen.add(request, response);
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
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request = new Request(processType, Request.START_CATCH,
				new String[] { "120","120" }, 1, 100);
		int[] rgb={200,300,300};
		Response response = new Response(processType, rgb,
				1, 100);
		try {
			bufferScreen.add(request);
			bufferScreen.add(request, response);
			assertEquals(response, bufferScreen.get(request));
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
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request = new Request(processType, Request.START_CATCH,
				new String[] { "300","300" }, 1, 200);
//		Response response = new Response(processType, "Connected",
//				1, 200);
		try {
			bufferScreen.add(request);
			//bufferCommands.add(request, response);
			assertNull(bufferScreen.get(request));
			
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
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request = new Request(processType, Request.SCREEN,
				new String[] { "300","300" }, 1, 240);
		Response response = new Response(processType, "Connected",
				1, 200);
		try {
			bufferScreen.add(request);
			request.setProcessType(new ProcessType(ProcessType.CONNECTION));
			response=bufferScreen.get(request);
			fail();
		} catch (BufferException e) {
			assertEquals("request.processType.not.consistent", e.getMessage());
		}

	}
	@Test
	public void testGetNext(){
		bufferScreen=new BufferScreen();
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request1,request2,request3,request4;
		try {
				request1 = new Request(processType, Request.START_CATCH,
					new String[] { "test" }, 1, 240);
				request2 = new Request(processType, Request.SCREEN,
						new String[] { "test" }, 1, 241);
				request3 = new Request(processType, Request.SCREEN,
						new String[] { "test" }, 1, 242);
				request4 = new Request(processType, Request.STOP_CATCH,
						new String[] { "test","test" }, 1, 243);
			try {
				bufferScreen.add(request1);
				bufferScreen.add(request3);
				bufferScreen.add(request2);
				bufferScreen.add(request4);
				assertEquals(request1,bufferScreen.getNext());
				assertEquals(request3,bufferScreen.getNext());
				assertEquals(request2,bufferScreen.getNext());
				assertEquals(request4,bufferScreen.getNext());
			} catch (BufferException e) {
				e.printStackTrace();
				fail();
			}
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void testFindRequest(){
		bufferScreen=new BufferScreen();
		ProcessType processType = new ProcessType(ProcessType.SCREEN);
		Request request1,request2,request3,request4;
		try {
				request1 = new Request(processType, Request.START_CATCH,
					new String[] { "test" }, 1, 240);
				request2 = new Request(processType, Request.SCREEN,
						new String[] { "test" }, 1, 241);
				request3 = new Request(processType, Request.SCREEN,
						new String[] { "test" }, 1, 242);
				request4 = new Request(processType, Request.STOP_CATCH,
						new String[] { "test","test" }, 1, 243);
			try {
				bufferScreen.add(request1);
				bufferScreen.add(request3);
				bufferScreen.add(request2);
				bufferScreen.add(request4);
				assertEquals(request1, bufferScreen.findRequestById(240));
				assertEquals(request4, bufferScreen.findRequestById(243));
				assertEquals(request2, bufferScreen.findRequestById(241));
				assertEquals(request3, bufferScreen.findRequestById(242));
			
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
