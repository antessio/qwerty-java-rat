package test.scheduler;

import static org.junit.Assert.*;

import managers.ProcessType;

import org.junit.Before;
import org.junit.Test;

import connection.Request;
import connection.exceptions.RequestException;
import connection.scheduler.AbstractScheduler;
import connection.scheduler.Scheduler;
import connection.scheduler.exceptions.SchedulerException;

public class TestScheduler {
	Scheduler scheduler;
	
	@Before 
	public void setUp(){
		scheduler=Scheduler.getInstance(null);
		
	}
	
	@Test
	public void testNextRequest() {
		Request [] requests=new Request[20];
		try {
				requests[0] = new Request(new ProcessType(ProcessType.COMMANDS), Request.GENERIC_COMMAND,
					new String[] { "dir" }, 1, 240);
				requests[1] = new Request(new ProcessType(ProcessType.FILES), Request.FILESYSTEM_ROOTS,
						new String[] { "test" }, 1, 241);
				requests[2] = new Request(new ProcessType(ProcessType.FILES), Request.DOWNLOAD,
						new String[] { "test" }, 1, 242);
				requests[3] = new Request(new ProcessType(ProcessType.COMMANDS), Request.GENERIC_COMMAND,
						new String[] { "netinterface" }, 1, 243);
				requests[4] = new Request(new ProcessType(ProcessType.COMMANDS), Request.GENERIC_COMMAND,
						new String[] { "cd" }, 1, 244);
				requests[5] = new Request(new ProcessType(ProcessType.SCREEN), Request.START_CATCH,
						new String[] { "test" }, 1, 245);
				requests[6] = new Request(new ProcessType(ProcessType.COMMANDS), Request.GENERIC_COMMAND,
						new String[] { "dir" }, 1, 246);
				requests[7] = new Request(new ProcessType(ProcessType.FILES), Request.FILESYSTEM_ROOTS,
						new String[] { "test" }, 1, 247);
				requests[8] = new Request(new ProcessType(ProcessType.FILES), Request.LIST_FILES,
						new String[] { "test","test" }, 1, 248);
				requests[9] = new Request(new ProcessType(ProcessType.SCREEN), Request.SCREEN,
						new String[] { "test" }, 1, 249);
				requests[10] = new Request(new ProcessType(ProcessType.COMMANDS), Request.GENERIC_COMMAND,
						new String[] { "dir" }, 1, 250);
				requests[11] = new Request(new ProcessType(ProcessType.FILES), Request.FILESYSTEM_ROOTS,
						new String[] { "test" }, 1, 251);
				requests[12] = new Request(new ProcessType(ProcessType.SCREEN), Request.SCREEN,
						new String[] { "test" }, 1, 252);
				requests[13] = new Request(new ProcessType(ProcessType.SCREEN), Request.STOP_CATCH,
						new String[] { "test","test" }, 1, 253);
				requests[14] = new Request(new ProcessType(ProcessType.COMMANDS), Request.GENERIC_COMMAND,
						new String[] { "dir" }, 1, 254);
				requests[15] = new Request(new ProcessType(ProcessType.FILES), Request.LIST_FILES,
							new String[] { "test","test" }, 1, 255);
				requests[16] = new Request(new ProcessType(ProcessType.SCREEN), Request.SCREEN,
						new String[] { "test" }, 1, 256);
				requests[17] = new Request(new ProcessType(ProcessType.SCREEN), Request.SCREEN,
						new String[] { "test" }, 1, 257);
				requests[18] = new Request(new ProcessType(ProcessType.FILES), Request.FILESYSTEM_ROOTS,
						new String[] { "test" }, 1, 258);
				requests[19] = new Request(new ProcessType(ProcessType.FILES), Request.DOWNLOAD,
						new String[] { "test" }, 1, 259);
				
				for(int i=0; i<requests.length;i++){
					try {
						scheduler.scheduleRequest(requests[i]);
					} catch (SchedulerException e) {
						fail();
					}
				}
				
				assertEquals(requests[5], scheduler.testNext());
				assertEquals(requests[9], scheduler.testNext());
				assertEquals(requests[12], scheduler.testNext());
				assertEquals(requests[13], scheduler.testNext());
				assertEquals(requests[16], scheduler.testNext());
				assertEquals(requests[17], scheduler.testNext());
				assertEquals(requests[1], scheduler.testNext());
				assertEquals(requests[2], scheduler.testNext());
				assertEquals(requests[7], scheduler.testNext());
				assertEquals(requests[8], scheduler.testNext());
				assertEquals(requests[0], scheduler.testNext());
				assertEquals(requests[3], scheduler.testNext());
				assertEquals(requests[4], scheduler.testNext());
				assertEquals(requests[6], scheduler.testNext());
				assertEquals(requests[11], scheduler.testNext());
				assertEquals(requests[15], scheduler.testNext());
				assertEquals(requests[18], scheduler.testNext());
				assertEquals(requests[19], scheduler.testNext());
				assertEquals(requests[10], scheduler.testNext());
				assertEquals(requests[14], scheduler.testNext());
				
		}catch(RequestException e){
			e.printStackTrace();
		}
		
	}

}
