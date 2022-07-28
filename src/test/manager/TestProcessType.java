package test.manager;
import static org.junit.Assert.*;

import org.junit.Test;

import managers.ProcessType;
public class TestProcessType {

	@Test
	public void testTypeToName() throws Exception {
		String t1=ProcessType.typeToName(ProcessType.CONNECTION);
		String t2=ProcessType.typeToName(ProcessType.COMMANDS);
		String t3=ProcessType.typeToName(ProcessType.FILES);
		String t4=ProcessType.typeToName(ProcessType.SCREEN);
		assertEquals(t1,"CONNECTION");
		assertEquals(t2,"COMMANDS");
		assertEquals(t3,"FILES");
		assertEquals(t4,"SCREEN");
	}
	@Test
	public void testNameToType() throws Exception {
		int t1=ProcessType.nameToType("CONNECTION");
		int t2=ProcessType.nameToType("COMMANDS");
		int t3=ProcessType.nameToType("FILES");
		int t4=ProcessType.nameToType("SCREEN");
		assertEquals(t1,ProcessType.CONNECTION);
		assertEquals(t2,ProcessType.COMMANDS);
		assertEquals(t3,ProcessType.FILES);
		assertEquals(t4,ProcessType.SCREEN);
	}
}
