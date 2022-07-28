package test.connection;

import static org.junit.Assert.*;

import org.junit.Test;

import connection.ClientState;

public class TestClientState {

	@Test
	public void testGetStateName() {
		ClientState cliState=new ClientState(ClientState.CONNECTED);
		assertEquals("Client connected to a server", cliState.getStateName());
		cliState.changeState(ClientState.WAITING_CONNECTIONS);
		assertEquals("Client started to receive connection", cliState.getStateName());
		cliState.changeState(ClientState.NOT_CONNECTED);
		assertEquals("Not Connected", cliState.getStateName());
	}

}
