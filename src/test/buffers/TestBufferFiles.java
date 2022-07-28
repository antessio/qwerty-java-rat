package test.buffers;

import static org.junit.Assert.*;

import managers.ProcessType;

import org.junit.Test;

import buffers.BufferCommands;
import buffers.BufferFactory;
import buffers.BufferFiles;
import buffers.exceptions.BufferException;
import connection.Request;
import connection.Response;
import connection.exceptions.RequestException;

public class TestBufferFiles {
	BufferFiles bufferFiles = (BufferFiles) BufferFactory
			.getFactory(new ProcessType(ProcessType.FILES));

	@Test
	/**
	 * Add correct
	 */
	public void testAdd1() throws RequestException {
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request = new Request(processType, Request.DOWNLOAD,
				new String[] { "file1.txt","file2.txt" }, 1, 1);
		try {
			bufferFiles.add(request);
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
				new String[] { "file1.txt","file2.txt" }, 1, 2);
		try {
			bufferFiles.add(request);
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
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request = new Request(processType, Request.DOWNLOAD,
				new String[] { "file1.txt","file2.txt" }, 1, 3);
		Response response = new Response(processType, "DOWNLOAD OK",
				1, 3);
		try {
			bufferFiles.add(request);
			bufferFiles.add(request, response);
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
				new String[] { "file1.txt","file2.txt" }, 1, 4);
		Response response = new Response(processType, "DOWNLOAD OK",
				1, 4);
		try {
			bufferFiles.add(request);
			bufferFiles.add(request, response);
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
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request = new Request(processType, Request.DOWNLOAD,
				new String[] { "file1.txt","file2.txt" }, 1, 5);
		Response response = new Response(new ProcessType(ProcessType.CONNECTION), "Connected",
				1, 5);
		try {
			bufferFiles.add(request);
			bufferFiles.add(request, response);
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
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request = new Request(processType, Request.DOWNLOAD,
				new String[] { "file1.txt","file2.txt" }, 1, 123);
		Response response = new Response(processType, "Connected",
				1, 123);
		try {
			bufferFiles.add(request, response);
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
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request = new Request(processType, Request.DOWNLOAD,
				new String[] { "file1.txt","file2.txt" }, 1, 100);
		Response response = new Response(processType, "Download ok",
				1, 100);
		try {
			bufferFiles.add(request);
			bufferFiles.add(request, response);
			assertEquals(response, bufferFiles.get(request));
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
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request = new Request(processType, Request.DOWNLOAD,
				new String[] { "file1.txt","file2.txt" }, 1, 200);
//		Response response = new Response(processType, "Connected",
//				1, 200);
		try {
			bufferFiles.add(request);
			//bufferFiles.add(request, response);
			assertNull(bufferFiles.get(request));
			
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
	public void testGet3() throws RequestException{
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request = new Request(processType, Request.FILESYSTEM_ROOTS,
				new String[] { "dir" }, 1, 240);
		Response response = new Response(processType, "Connected",
				1, 200);
		try {
			bufferFiles.add(request);
			request.setProcessType(new ProcessType(ProcessType.CONNECTION));
			response=bufferFiles.get(request);
			fail();
		} catch (BufferException e) {
			assertEquals("request.processType.not.consistent", e.getMessage());
		}

	}
	@Test
	public void testGetNext(){
		bufferFiles=new BufferFiles();
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request1,request2,request3,request4;
		try {
				request1 = new Request(processType, Request.FILESYSTEM_ROOTS,
					new String[] { "test" }, 1, 240);
				request2 = new Request(processType, Request.DOWNLOAD,
						new String[] { "test" }, 1, 241);
				request3 = new Request(processType, Request.FILESYSTEM_ROOTS,
						new String[] { "test" }, 1, 242);
				request4 = new Request(processType, Request.LIST_FILES,
						new String[] { "test","test" }, 1, 243);
			try {
				bufferFiles.add(request1);
				bufferFiles.add(request3);
				bufferFiles.add(request2);
				bufferFiles.add(request4);
				assertEquals(request1,bufferFiles.getNext());
				assertEquals(request3,bufferFiles.getNext());
				assertEquals(request2,bufferFiles.getNext());
				assertEquals(request4,bufferFiles.getNext());
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
		bufferFiles=new BufferFiles();
		ProcessType processType = new ProcessType(ProcessType.FILES);
		Request request1,request2,request3,request4;
		try {
				request1 = new Request(processType, Request.FILESYSTEM_ROOTS,
					new String[] { "test" }, 1, 240);
				request2 = new Request(processType, Request.DOWNLOAD,
						new String[] { "test" }, 1, 241);
				request3 = new Request(processType, Request.FILESYSTEM_ROOTS,
						new String[] { "test" }, 1, 242);
				request4 = new Request(processType, Request.LIST_FILES,
						new String[] { "test","test" }, 1, 243);
			try {
				bufferFiles.add(request1);
				bufferFiles.add(request3);
				bufferFiles.add(request2);
				bufferFiles.add(request4);
				assertEquals(request1, bufferFiles.findRequestById(240));
				assertEquals(request4, bufferFiles.findRequestById(243));
				assertEquals(request2, bufferFiles.findRequestById(241));
				assertEquals(request3, bufferFiles.findRequestById(242));
				
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
