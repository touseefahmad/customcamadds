package citrusbits.com.customcamera;

import java.util.Observable;

public class DataObservor extends Observable{
	protected void triggerObservers(){
		setChanged();
		notifyObservers();
	}


}
