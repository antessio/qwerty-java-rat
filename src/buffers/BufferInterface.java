package buffers;

import buffers.exceptions.BufferException;
import connection.Request;
import connection.Response;

/**
 * Defines an interface for buffers. 
 * @author antessio
 *
 */
public interface BufferInterface {
	
	/**
	 * Adds a request to the buffer.
	 * @param request Request
	 * @throws BufferException When the type of process is not consistent with the type of buffer (with message: "request.processType.not.consistent")
	 * 							or when is not possible to write in buffer (with message: "cannot.add.to.buffer")
	 */
	public void add(Request request)throws BufferException;
	
	/**
	 * Adds a request to the buffer.
	 * @param request Request
	 * @param response Response response to the request.
	 * @throws BufferException When the type of process, in request, is not consistent with the type of buffer (with message: "request.processType.not.consistent"),
	 * 							when is not possible to write in buffer (with message: "cannot.add.to.buffer"),
	 * 							when the type of process, in response, is not consistent with the type of buffer (with message: "response.processType.not.consistent")
	 * 							or when there is no request associated (with message="no.request")
	 */
	public void add(Request request, Response response)throws BufferException;
	
	/**
	 * Get the response, associated to the specified request, from the buffer.
	 * @param request Request 
	 * @throws BufferException When the type of process is not consistent with the type of buffer(with message: "request.processType.not.consistent")
	 * 			or when is impossible to read from buffer (with message: "cannot.read.from.buffer");
	 * 			
	 * @return Response to the specified request or null if there is no response.
	 */
	public Response get(Request request)throws BufferException;

	/**
	 * Says if the buffer is empty.
	 * @return <c>true</c> if empty, <c>false</c> otherwise
	 */
	public boolean isEmpty();
	
	/**
	 * Says if the buffer is full.
	 * @return <c>true</c> if full, <c>false</c> otherwise
	 */
	public boolean isFull();
	
	/**
	 * Return the next request to handle/send.
	 * @return Request
	 */
	public Request getNext();

	/**
	 * Return the request with the specified id.
	 * @param id Request id.
	 * @return The request with the specified id or null if it does not found
	 */
	public Request findRequestById(int id);
}
