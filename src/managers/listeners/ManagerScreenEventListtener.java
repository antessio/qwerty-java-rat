package managers.listeners;

import java.util.EventListener;

import managers.events.ScreenEvent;

public abstract class ManagerScreenEventListtener implements EventListener{
	
	/**
	 * When the client receive a screen capture. 
	 * @param evt
	 */
	public abstract void newScreen(ScreenEvent evt);

}
