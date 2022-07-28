package test.connection;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import managers.ProcessType;

import org.junit.After;
import org.junit.Test;
import connection.QwertyConnection;
import connection.Request;
import connection.Response;
import connection.exceptions.ConnectionException;
import connection.exceptions.ProtocolException;
import connection.exceptions.RequestException;

public class TestQWERTY {
	
	String password="antessio";

	@After
	public void tearDown(){
		QwertyConnection.resetConnection();
	}
	
	@Test
	/**
	 * Open Connection Correct
	 */
	public void testOpenConnectionCorrect() {
		TestConnectionThread t = new TestConnectionThread(1);
		QwertyConnection connection;
		t.start();
		ServerSocket client = null;
		try {
			client = new ServerSocket(4000);
			Socket received=client.accept();
			connection =QwertyConnection.getInstance(received, password);
			connection.open();
			assertTrue(connection.isOpen());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			t.stopTest();
			t.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = null;
		}
	}
	
	@Test
	public void testOpenConnectionWrong() {
		QwertyConnection connection;
		TestConnectionThread t = new TestConnectionThread(2);
		t.start();
		ServerSocket client = null;
		try {
			client = new ServerSocket(4000);
			connection = QwertyConnection.getInstance(client.accept(),password);
			assertFalse(connection.isOpen());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			t.stopTest();
			t.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = null;
		}
	}
	@Test
	/**
	 * Test connect correct, right password
	 */
	public void testConnectCorrect() {
		QwertyConnection connection;
		TestConnectionThread t = new TestConnectionThread(3);
		t.start();
		ServerSocket client = null;
		try {
			client = new ServerSocket(4000);
			connection = QwertyConnection.getInstance(client.accept(),password);
			connection.open();
			assertTrue(connection.connect());
			assertTrue(connection.isConnected());
		} catch (IOException ex) {
			fail();
		} catch (ConnectionException e) {
			e.printStackTrace();
			fail();
		} finally {
			t.stopTest();
			t.closeStreams();
			t.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = null;
		}
	}

	@Test
	/**
	 * Test connect correct: wrong password, connection not open
	 */
	public void testConnectCorrect2() {
		QwertyConnection connection;
		TestConnectionThread t = new TestConnectionThread(4);
		t.start();
		ServerSocket client = null;
		try {
			client = new ServerSocket(4000);
			connection = QwertyConnection.getInstance(client.accept(),password);
			connection.open();
			assertFalse(connection.connect());
			assertFalse(connection.isConnected());
		} catch (IOException ex) {
			fail();
		} catch (ConnectionException e) {
			e.printStackTrace();
			fail();
		} finally {
			t.stopTest();
			t.closeStreams();
			t.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = null;
		}
	}
	
	@Test
	/**
	 * Test connect wrong: connection not open, catch exception
	 */
	public void testConnectWrong(){
		QwertyConnection connection;
		TestConnectionThread t = new TestConnectionThread(1);
		t.start();
		ServerSocket client = null;
		try {
			client = new ServerSocket(4000);
			connection = QwertyConnection.getInstance(client.accept(),password);
			connection.connect();
			fail();
		} catch (IOException ex) {
			fail();
		} catch (ConnectionException e) {
			assertEquals("connection.must.be.open", e.getMessage());
		} finally {
			t.stopTest();
			t.closeStreams();
			t.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = null;
		}
	}
	
	@Test
	/**
	 * Test connect wrong: client already connected, catch exception
	 */
	public void testConnectWrong2(){
		QwertyConnection connection;
		TestConnectionThread t = new TestConnectionThread(3);
		t.start();
		ServerSocket client = null;
		try {
			client = new ServerSocket(4000);
			connection = QwertyConnection.getInstance(client.accept(),password);
			connection.open();
			connection.connect();
			connection.connect();
			fail();
		} catch (IOException ex) {
			fail();
		} catch (ConnectionException e) {
			assertEquals("already.connected", e.getMessage());
		} finally {
			t.stopTest();
			t.closeStreams();
			t.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = null;
		}
	}

	@Test
	/**
	 * Test connect wrong: try to catch IOException
	 */
	public void testConnectWrong3(){
		QwertyConnection connection;
		TestConnectionThread t = new TestConnectionThread(5);
		t.start();
		ServerSocket client = null;
		try {
			client = new ServerSocket(4000);
			connection = QwertyConnection.getInstance(client.accept(),password);
			connection.open();
			connection.connect();
			fail();
		} catch (IOException ex) {
			fail();
		} catch (ConnectionException e) {
			assertEquals("io.exception", e.getMessage());
		} finally {
			t.stopTest();
			t.closeStreams();
			t.interrupt();
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = null;
		}
	}
		@Test
		/**
		 * Test send receive correct: PING -> PONG
		 */
		public void testPing(){
			QwertyConnection connection;
			TestConnectionThread t = new TestConnectionThread(6);
			t.start();
			ServerSocket client = null;
			try {
				client = new ServerSocket(4000);
				connection = QwertyConnection.getInstance(client.accept(),password);
				connection.open();
				if(connection.isOpen()){
					boolean connected=connection.connect();
					String[] params=new String[0];
					if(connected && connection.isConnected()){
						Request ping;
						try {
							assertEquals(true,connection.ping());
							ping = new Request(new ProcessType(ProcessType.CONNECTION), Request.PING, params, 112, 112);
//							connection.send(ping);
//							Response pong=(Response)connection.receive();
//							assertEquals(pong.getReceived(),Response.PONG);
						} catch (RequestException e) {
							fail();
						}
				}
				
						
				
				}
				
			} catch (IOException ex) {
				fail();
			} catch (ConnectionException e) {
				fail();
			} finally {
				t.stopTest();
				t.closeStreams();
				t.interrupt();
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				t = null;
			}
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
//								e.printStackTrace();
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
									running=false;
								}
								
							} catch (RequestException e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
							}
						} catch (ClassNotFoundException e) {
							running=false;
						}
					}
				break;
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}

	}
	
}
