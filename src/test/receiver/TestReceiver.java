package test.receiver;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import managers.ProcessType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import buffers.BufferCommands;
import buffers.BufferFactory;
import buffers.exceptions.BufferException;
import connection.QwertyConnection;
import connection.Request;
import connection.Response;
import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;
import connection.exceptions.RequestException;
import connection.receiver.Receiver;

public class TestReceiver {

	Receiver receiver;
	QwertyConnection connection;
	String password="antessio";
	ServerSocket client = null;
	@Before
	public void setUp() throws Exception {
		TestConnectionThread t = new TestConnectionThread(6);
		t.start();
		client = new ServerSocket(4000);
		Socket received=client.accept();
		QwertyConnection.resetConnection();
		connection =QwertyConnection.getInstance(received, password);
		connection.open();
		receiver=new Receiver(connection);
		}

	@After
	public void tearDown() throws Exception {
//		String [] parameters=new String[0];
//		Request close =new Request(new ProcessType(ProcessType.CONNECTION), Request.CLOSE, parameters, 1, 1);
//		connection.send(close);
		client.close();
		Thread.sleep(1000);
		connection.close();
		QwertyConnection.resetConnection();
	}

	@Test
	public void testRequestCommands() throws InterruptedException {
		ProcessType processType = new ProcessType(ProcessType.COMMANDS,
				ProcessType.typeToName(ProcessType.COMMANDS));
		try {
			if(connection.connect()){
				new Thread(receiver).start();
				//receiver.run();
				Request request = new Request(processType, Request.NET_INTERFACES,
						new String[] { "TEST" }, 1, 1);
				
				try{
					BufferFactory.getFactory(processType).add(request);
					connection.send(request);
					Thread.sleep(2000);
					Response expected=new Response();
					expected.setProcessType(request.getProcessType());
					expected.setResponseId(request.getIdRequest());
					expected.setSender(request.getSender());
					expected.setReceived("TEST COMMANDS");
					assertEquals(expected, BufferFactory.getFactory(processType).get(request));
				}catch(BufferException e){
					fail();
				}
			}else{
				fail("CONNECTION NOT OPENED");
			}
		} catch (RequestException e) {
			fail();
			e.printStackTrace();
		}catch(ConnectionException e){
			e.printStackTrace();
			fail(e.getMessage());
		}catch(ProtocolException e){
			fail();
			e.printStackTrace();
		}
	}

}
class TestConnectionThread extends Thread{

	private ObjectInputStream iStreamServer;
	private ObjectOutputStream oStreamServer;
	private Socket server;
	private int test;
	private boolean running;

	public TestConnectionThread(int test) {
		this.test = test;
	}

	public void closeStreams(){
		try {
			if (iStreamServer != null)
				iStreamServer.close();
			if (oStreamServer != null)
				oStreamServer.close();
			if (server != null)
				server.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void stopTest() {
		running = false;
	}

	@Override
	public void run() {

		try {
			switch (test) {
				case 1:
					//test open connection correct
					server = new Socket("localhost", 4000);
					iStreamServer = new ObjectInputStream(server.getInputStream());
					oStreamServer = new ObjectOutputStream(server.getOutputStream());
					break;
				case 2:
					//test open connection wrong (does not open streams)
					server = new Socket("localhost", 4000);
					break;
				case 3:
					//test connection correct
					running = true;
					server = new Socket("localhost", 4000);
					iStreamServer = new ObjectInputStream(server.getInputStream());
					oStreamServer = new ObjectOutputStream(server.getOutputStream());
					String command = "";
					while (running) {
						// reads a command
						try {
							String msg=(String)iStreamServer.readObject();
							try {
								Request currRequest=Request.createRequestFromString(msg);
								if(currRequest.getCommand().equals(Request.CONNECT)){
									String password="antessio";
									Response response=new Response();
									response.setProcessType(currRequest.getProcessType());
									response.setResponseId(currRequest.getIdRequest());
									response.setSender(currRequest.getSender());
									if(currRequest.getParameters()[0].equals(password)){
										response.setReceived(Response.OK);											
									}else{
										response.setReceived(Response.NOK);
									}
									oStreamServer.writeObject(response);
									oStreamServer.flush();
									running=false;
								}
								
							} catch (RequestException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (ClassNotFoundException e) {
							running=false;
						}
					}
					break;
				
				case 4: 
					//test connect correct password does not correspond 
					running = true;
					server = new Socket("localhost", 4000);
					iStreamServer = new ObjectInputStream(server.getInputStream());
					oStreamServer = new ObjectOutputStream(server.getOutputStream());
					while (running) {
						// reads a command
						try {
							String msg=(String)iStreamServer.readObject();
							try {
								Request currRequest=Request.createRequestFromString(msg);
								if(currRequest.getCommand().equals(Request.CONNECT)){
									String password="WRONGABCDE";
									Response response=new Response();
									response.setProcessType(currRequest.getProcessType());
									response.setResponseId(currRequest.getIdRequest());
									response.setSender(currRequest.getSender());
									if(currRequest.getParameters()[0].equals(password)){
										response.setReceived(Response.OK);											
									}else{
										response.setReceived(Response.NOK);
									}
									oStreamServer.writeObject(response);
									oStreamServer.flush();
									running=false;
								}
								
								
							} catch (RequestException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (ClassNotFoundException e) {
							running=false;
						}
					}
					break;
					
				case 5: 
					//test connect correct password does not correspond 
					running = true;
					server = new Socket("localhost", 4000);
					iStreamServer = new ObjectInputStream(server.getInputStream());
					oStreamServer = new ObjectOutputStream(server.getOutputStream());
					iStreamServer.close();
				break;
				
				case 6:
					//test connection correct
					running = true;
					server = new Socket("localhost", 4000);
					iStreamServer = new ObjectInputStream(server.getInputStream());
					oStreamServer = new ObjectOutputStream(server.getOutputStream());
					while (running) {
						// reads a command
						try {
							String msg=(String)iStreamServer.readObject();
							try {
								Request currRequest=Request.createRequestFromString(msg);
								if(currRequest.getCommand().equals(Request.CONNECT)){
									String password="antessio";
									Response response=new Response();
									response.setProcessType(currRequest.getProcessType());
									response.setResponseId(currRequest.getIdRequest());
									response.setSender(currRequest.getSender());
									boolean connected=false;
									if(currRequest.getParameters()[0].equals(password)){
										response.setReceived(Response.OK);
										connected=true;
									}else{
										connected=false;
										response.setReceived(Response.NOK);
									}
									oStreamServer.writeObject(response);
									oStreamServer.flush();	
									//running=false;
								}
								
								if(currRequest.getCommand().equals(Request.DOWNLOAD)){
									Response response3=new Response();
									response3.setProcessType(currRequest.getProcessType());
									response3.setResponseId(currRequest.getIdRequest());
									response3.setSender(currRequest.getSender());
									response3.setReceived("TEST DOWNLOAD");
									oStreamServer.writeObject(response3);
									oStreamServer.flush();
									
								}
								if(currRequest.getCommand().equals(Request.SCREEN)){
									Response response3=new Response();
									response3.setProcessType(currRequest.getProcessType());
									response3.setResponseId(currRequest.getIdRequest());
									response3.setSender(currRequest.getSender());
									response3.setReceived("TEST SCREEN");
									oStreamServer.writeObject(response3);
									oStreamServer.flush();
								}
								if(currRequest.getCommand().equals(Request.NET_INTERFACES)){
									Response response3=new Response();
									response3.setProcessType(currRequest.getProcessType());
									response3.setResponseId(currRequest.getIdRequest());
									response3.setSender(currRequest.getSender());
									response3.setReceived("TEST COMMANDS");
									oStreamServer.writeObject(response3);
									oStreamServer.flush();
								}
								
								if(currRequest.getCommand().equals(Request.CLOSE)){
									stopTest();
									closeStreams();
								}
							} catch (RequestException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (ClassNotFoundException e) {
							running=false;
						}
					}
				break;
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	
}
