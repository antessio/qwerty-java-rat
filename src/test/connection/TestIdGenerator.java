package test.connection;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import connection.RequestIdGenerator;

public class TestIdGenerator {

	RequestIdGenerator idGenerator;
	
	@Before
	public void setUp() {
		RequestIdGenerator.reset();
		idGenerator=RequestIdGenerator.getInstance();

	}
	
	@After
	public void tearDown(){
		RequestIdGenerator.reset();
	}
	
	@Test
	public void testNextId1() {
		
		assertEquals(idGenerator.getNextId(), 1);
		assertEquals(idGenerator.getNextId(), 2);
		assertEquals(idGenerator.getNextId(), 3);
	}
	
	@Test
	public void testExpiredRequest1() {
		idGenerator.getNextId();
		idGenerator.getNextId();
		idGenerator.getNextId();
		idGenerator.requestExpired(2);
		assertEquals(idGenerator.getNextId(),2);
	}
	@Test
	public void testExpiredRequest2() {
		idGenerator.getNextId();
		idGenerator.getNextId();
		idGenerator.getNextId();
		idGenerator.requestExpired(2);
		assertEquals(2,idGenerator.getNextId());
		assertEquals(4, idGenerator.getNextId());
	}
	

}
