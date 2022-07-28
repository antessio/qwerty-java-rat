package test.connection;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import managers.ProcessType;

import org.junit.Test;

import connection.Request;
import connection.Response;
import connection.ServerState;
import connection.exceptions.RequestException;

public class TestServerState {

	private Socket server;
	private ObjectInputStream iStreamServer;
	private ObjectOutputStream oStreamServer;
	
	@Test
	public void testGetStateName() {
		ServerState serState=new ServerState(ServerState.CLIENT_CONNECTED);
		assertEquals("Connected", serState.getStateName());
		serState.changeState(ServerState.CLIENT_DISCONNECTED);
		assertEquals("Client disconnected", serState.getStateName());
		serState.changeState(ServerState.CONNECTION_CLOSED);
		assertEquals("Connection Closed", serState.getStateName());
	}
	@Test
	public void testStateConnected(){
		TestStateThread testClient=new TestStateThread(4);	
		testClient.start();
		try {
			server = new Socket("localhost", 4000);
			System.out.println("Test: Client Online?");
			iStreamServer = new ObjectInputStream(server.getInputStream());
			oStreamServer = new ObjectOutputStream(server.getOutputStream());
			String command = "";
			// reads a command
			System.out.println("Test: Connection Alive?");
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
						boolean running=true;
						while(running){
							String ping=(String)iStreamServer.readObject();
							Request pingRequest=Request.createRequestFromString(ping);
							if(pingRequest.getCommand().equals(Request.PING)){
								Response pong=new Response();
								pong.setProcessType(currRequest.getProcessType());
								pong.setResponseId(currRequest.getIdRequest());
								pong.setSender(currRequest.getSender());
								pong.setReceived(Response.PONG);			
								oStreamServer.writeObject(pong);
								oStreamServer.flush();
							}
							
						}
					}
					
				} catch (RequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch(IOException e){
				e.printStackTrace();
				fail(e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
	}

}
/**
 * This class allows to simulate a client.
 * @author antessio
 *
 */
class TestStateThread extends Thread {

	private ObjectInputStream iStreamClient;
	private ObjectOutputStream oStreamClient;
	private ServerSocket client;
	private int test;
	private boolean running;

	public TestStateThread(int test) {
		this.test = test;
	}

	public void closeStreams(){
		try {
			if (iStreamClient != null)
				iStreamClient.close();
			if (oStreamClient != null)
				oStreamClient.close();
			if (client != null)
				client.close();
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
		String [] parameters=null;
		try {
			switch (test) {
				case 1:
					//test1: client receives connection and open streams (should be connection alive)
					client = new ServerSocket(4000);
					Socket s1=client.accept();
					oStreamClient = new ObjectOutputStream(s1.getOutputStream());
					iStreamClient = new ObjectInputStream(s1.getInputStream());
					Thread.sleep(5000);
					System.out.println("Thread Server State Test: Connection Alive");
					break;
				case 2:
					//test2: client creates the socket but doesn't create streams (should be client offline) 
					client = new ServerSocket(4000);
					Thread.sleep(5000);
					System.out.println("Thread Server State Test: Client Offline");
					break;
				case 3:
					//test3: client creates the socket, the streams and send the message to connect
					//(should be client connected)
					System.out.println("Y");
					client = new ServerSocket(4000);
					System.out.println("Socket creata");
					Socket s2=client.accept();
					System.out.println("Accepted: "+s2);
					oStreamClient = new ObjectOutputStream(s2.getOutputStream());
					iStreamClient = new ObjectInputStream(s2.getInputStream());
					parameters=new String[1];
					parameters[0]="antessio";
					try {
						Request request=new Request(new ProcessType(ProcessType.CONNECTION), Request.CONNECT,parameters , 0, 0);
						oStreamClient.writeObject(request.toString());
						oStreamClient.flush();
						Response response=(Response)iStreamClient.readObject();
						if(response.getReceived().equals(Response.OK)){
							System.out.println("Thread Server State Test: Connected");
						}else{
							System.out.println("Thread Server State Test: Not Connected");
						}
						Thread.sleep(2000);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
					break;	
				
				case 4: 
					//test 4: client creates the socket, the streams and send the message to connect. 
					//After 3 seconds happens a connection fault. (should be client disconnected)
					running = true;
					client = new ServerSocket(4000);
					Socket s3=client.accept();
					oStreamClient = new ObjectOutputStream(s3.getOutputStream());
					iStreamClient = new ObjectInputStream(s3.getInputStream());
					parameters=new String[1];
					parameters[0]="antessio";
					try {
						Request request=new Request(new ProcessType(ProcessType.CONNECTION), Request.CONNECT,parameters , 0, 0);
						oStreamClient.writeObject(request.toString());
						oStreamClient.flush();
						Response response=(Response)iStreamClient.readObject();
						if(response.getReceived().equals(Response.OK)){
							System.out.println("Connected");
						}else{
							System.out.println("Not Connected");
						}
						Thread.sleep(3000);
						closeStreams();
						System.out.println("Thread Server State Test: Disconnected");
					}catch (Exception e) {
						e.printStackTrace();
					}	
					break;
					
				}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}

	}
}