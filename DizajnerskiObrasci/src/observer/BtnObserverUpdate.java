package observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import mvc.DrawingFrame;

public class BtnObserverUpdate implements PropertyChangeListener{
	
	//observer
	private DrawingFrame frame;
	
	public BtnObserverUpdate (DrawingFrame frame) {
		this.frame = frame;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("deleteBtn")) {
			frame.getBtnDelete().setEnabled((boolean) evt.getNewValue());
		}
		if(evt.getPropertyName().equals("modifyBtn")) {
			frame.getBtnModify().setEnabled((boolean) evt.getNewValue());
		}
		if(evt.getPropertyName().equals("selectBtn")) {
			frame.getTglBtnSelect().setEnabled((boolean) evt.getNewValue());
		}
		if(evt.getPropertyName().equals("bringToFrontBtn")) {
			frame.getBtnBringToFront().setEnabled((boolean) evt.getNewValue());
		}
		if(evt.getPropertyName().equals("bringToBackBtn")) {
			frame.getBtnBringToBack().setEnabled((boolean) evt.getNewValue());
		}
		if(evt.getPropertyName().equals("toFrontBtn")) {
			frame.getBtnToFront().setEnabled((boolean) evt.getNewValue());
		}
		if(evt.getPropertyName().equals("toBackBtn")) {
			frame.getBtnToBack().setEnabled((boolean) evt.getNewValue());
		}
	}
}
