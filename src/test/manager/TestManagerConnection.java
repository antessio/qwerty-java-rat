package test.manager;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import managers.ManagerConnection;
import managers.ProcessType;
import managers.events.ConnectionEvent;
import managers.exceptions.ManagerException;
import managers.listeners.ManagerConnectionEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import connection.Request;
import connection.Response;
import connection.ServerInfo;
import connection.exceptions.RequestException;

public class TestManagerConnection {

	private ManagerConnection manager;
	private TestConnectionThread test;
	@Before
	public void setUp() throws Exception {
		test=new TestConnectionThread(0);
		
	}

	@After
	public void tearDown() throws Exception {
		manager.resetManager();
	}

	/*
	 * 1. create connection
	 * 2. getConnections
	 * 3. aggiungere listener e vedere se la connessione è attiva.
	 */
	@Test
	public void testReceiveConnections() {
		manager=ManagerConnection.getInstance();
		manager.addEventListener(new ConnectionEventListener());
		manager.configure(4000);
		test.start();
		List<ServerInfo> connections;
		try {
			connections = manager.getConnections();
			System.out.println("-----Connection List -----");
			assertEquals(1, connections.size());
			for (ServerInfo serverInfo : connections) {
				System.out.println(serverInfo);
			}
			assertTrue(manager.createConnection(0, "antessio"));
			String [] parameters=new String[0];
			manager.handle(Request.CONNECT, parameters);
			try {
				Thread.sleep(4000);
				manager.handle(Request.CLOSE, parameters);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ManagerException e) {
			fail(e.getMessage());
		}
	}

}

class ConnectionEventListener extends ManagerConnectionEventListener{

	@Override
	public void newConnection(ConnectionEvent evt) {
		assertEquals("Connection Created",evt.getMessage());
		System.out.println(evt.getMessage());
		System.out.println(evt.geteResponse());
		System.out.println(evt.getServerInfo());
	}

	@Override
	public void connectionClosed(ConnectionEvent evt) {	
		System.out.println(evt.getMessage());
	}

	@Override
	public void connectionFault(ConnectionEvent evt) {
		fail(evt.getMessage());
	}

	@Override
	public void connectionReceived(ConnectionEvent evt) {
		assertEquals("New Connection Received",evt.getMessage());
		System.out.println(evt.getMessage());
		System.out.println(evt.geteResponse());
		System.out.println(evt.getServerInfo());
//		assertEquals(4000,evt.getServerInfo().getPort());
		
	}
	
}

class TestConnectionThread extends Thread {

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
									if(connected){
										System.out.println("Connected");
										String msg2=(String)iStreamServer.readObject();
										Request secondRequest=Request.createRequestFromString(msg2);
										if(secondRequest.getCommand().equals(Request.PING)){
											Response response2=new Response();
											response2.setProcessType(secondRequest.getProcessType());
											response2.setResponseId(secondRequest.getIdRequest());
											response2.setSender(secondRequest.getSender());
											response2.setReceived(Response.PONG);
											oStreamServer.writeObject(response2);
											oStreamServer.flush();
											System.out.println("PONG");
										}
									}
//									running=false;
								}
								if(currRequest.getCommand().equals(Request.PING)){
									Response pong=new Response();
									pong.setProcessType(currRequest.getProcessType());
									pong.setResponseId(currRequest.getIdRequest());
									pong.setSender(currRequest.getSender());
									pong.setReceived(Response.PONG);
									oStreamServer.writeObject(pong);
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
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}

