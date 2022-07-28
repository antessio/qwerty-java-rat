package connection;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * This class allows to keep the uniqueness between the requests stored in buffers. Generates an id for the request.
 * Manages the reuse of id. 
 * @author antessio
 *
 */
public class RequestIdGenerator {

	private static RequestIdGenerator instance;
	private List<Integer> available;
	int contId=0;

	public static void reset(){
		instance=null;
	}
	
	private RequestIdGenerator(){
		available=new LinkedList<Integer>();
	}
	/**
	 * Get the unique instance of idGenerator
	 * @return Instance
	 */
	public static RequestIdGenerator getInstance(){
		if(instance==null)
			instance= new RequestIdGenerator();
		return instance;
	}
	
	/**
	 * Returns the next id to set in the request.
	 * @return Id
	 */
	public int getNextId(){
		if(available.isEmpty()){
			contId++;
			return contId;
		}else{
			return available.remove(0);
		}
	}
	
	/**
	 * Inserts the id in available list.
	 * @param id Id of request handled.
	 */
	public void requestExpired(int id){
		if(available==null)
			available=new Stack<Integer>();
		available.add(id);
	}
	
}
