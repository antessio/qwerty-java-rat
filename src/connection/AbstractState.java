package connection;

/**
 * This class represents the state of connection.
 * @author antessio
 */
public abstract class AbstractState {
	protected int state;
	
	/**
	 * Constructor
	 * @param state
	 */
	public AbstractState(int state){
		this.state=state;
	}
	
	/**
	 * Returns an integer value representing the state.
	 * @return
	 */
	public int getState(){
		return state;
	}
	
	/**
	 * This method changes the state.
	 * @param state 
	 */
	public void changeState(int state){
		this.state=state;
	}
	/**
	 * Returns the name associated to the state. 
	 * @return State's name.
	 */
	public abstract  String getStateName();
	
	

}
