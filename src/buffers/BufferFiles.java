package buffers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import managers.ProcessType;

import buffers.exceptions.BufferException;

import connection.Request;
import connection.Response;

public class BufferFiles implements BufferInterface{

	private Map<Request,Response> buffer;
	private LinkedList<Request> requestQueue;
	
	public BufferFiles(){
		buffer=new HashMap<Request,Response>();
		requestQueue=new LinkedList<Request>();
	}
	@Override
	public void add(Request request)throws BufferException {
		if(request.getProcessType()==null || request.getProcessType().getType()!=ProcessType.FILES)
			throw new BufferException("request.processType.not.consistent");
		try{
			buffer.put(request,null);
			requestQueue.addLast(request);
		}catch(UnsupportedOperationException ex){
			new BufferException("cannot.add.to.buffer",ex);
			
		}catch(ClassCastException ex){
			throw new BufferException("cannot.add.to.buffer",ex);
		}catch(IllegalArgumentException ex){
			throw new BufferException("cannot.add.to.buffer",ex);
		}catch(NullPointerException ex){
			throw new BufferException("cannot.add.to.buffer",ex);
		}

	}

	@Override
	public Response get(Request request) throws BufferException{
		if(request.getProcessType()==null || request.getProcessType().getType()!=ProcessType.FILES)
			throw new BufferException("request.processType.not.consistent");
		try{
//			if(!buffer.containsKey(request))
//				return null;
			return buffer.remove(request);
		}catch(ClassCastException ex){
			throw new BufferException("cannot.read.from.buffer",ex);
		}catch(NullPointerException ex){
			throw new BufferException("cannot.read.from.buffer",ex);
		}catch(UnsupportedOperationException ex){
			throw new BufferException("cannot.read.from.buffer",ex);
		}

	}
	@Override
	public void add(Request request, Response response) throws BufferException {
		if(request.getProcessType()==null || request.getProcessType().getType()!=ProcessType.FILES)
			throw new BufferException("request.processType.not.consistent");
		if(response.getProcessType()==null || response.getProcessType().getType()!=ProcessType.FILES)
			throw new BufferException("response.processType.not.consistent");
		if(!buffer.containsKey(request))
			throw new BufferException("no.request");
		try{
			buffer.put(request,response);
		}catch(UnsupportedOperationException ex){
			new BufferException("cannot.add.to.buffer",ex);
			
		}catch(ClassCastException ex){
			throw new BufferException("cannot.add.to.buffer",ex);
		}catch(IllegalArgumentException ex){
			throw new BufferException("cannot.add.to.buffer",ex);
		}catch(NullPointerException ex){
			throw new BufferException("cannot.add.to.buffer",ex);
		}

		
	}
	@Override
	public boolean isEmpty() {
		return requestQueue.isEmpty();
	}
	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Request getNext() {
		return requestQueue.removeFirst();
	}

	@Override
	public Request findRequestById(int id) {
		for(Iterator<Entry<Request, Response>> it=buffer.entrySet().iterator(); it.hasNext();){
			Map.Entry<Request, Response> curr=it.next();
			if(curr.getKey().getIdRequest()==id)
				return curr.getKey();
		}
		return null;
	}
}
