package managers.listeners;

import java.util.EventListener;

import managers.events.FilesEvent;

public abstract class ManagerFilesEventListener implements EventListener{

	public abstract void fileDownloaded(FilesEvent evt);
	
}
