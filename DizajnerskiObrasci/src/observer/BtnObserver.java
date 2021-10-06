package observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BtnObserver {
	
	private boolean selectBtnEnabled;
	private boolean deleteBtnEnabled;
	private boolean modifyBtnEnabled;
	private boolean bringToFrontBtnEnabled;
	private boolean bringToBackBtnEnabled;
	private boolean toBackBtnEnabled;
	private boolean toFrontBtnEnabled;
	
	//observable
	
	private PropertyChangeSupport propertyChangeSupport;
	
	public BtnObserver() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener (PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}
	
	public void removeProperyChangeListener (PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
	}
	
	public void setDeleteBtnEnabled (boolean deleteBtnEnabled) {
		propertyChangeSupport.firePropertyChange("deleteBtn", this.deleteBtnEnabled, deleteBtnEnabled);
		this.deleteBtnEnabled = deleteBtnEnabled;
	}
	
	public void setModifyBtnEnabled (boolean modifyBtnEnabled) {
		propertyChangeSupport.firePropertyChange("modifyBtn", this.modifyBtnEnabled, modifyBtnEnabled);
		this.modifyBtnEnabled = modifyBtnEnabled;
	}
	
	public void setSelectBtnEnabled (boolean selectBtnEnabled) {
		propertyChangeSupport.firePropertyChange("selectBtn", this.selectBtnEnabled, selectBtnEnabled);
		this.selectBtnEnabled = selectBtnEnabled;
	}
	
	public void setBringToFrontBtnEnabled (boolean bringToFrontBtnEnabled) {
		propertyChangeSupport.firePropertyChange("bringToFrontBtn", this.bringToFrontBtnEnabled, bringToFrontBtnEnabled);
		this.bringToFrontBtnEnabled = bringToFrontBtnEnabled;
	}
	
	public void setBringToBackBtnEnabled (boolean bringToBackBtnEnabled) {
		propertyChangeSupport.firePropertyChange("bringToBackBtn", this.bringToBackBtnEnabled, bringToBackBtnEnabled);
		this.bringToBackBtnEnabled = bringToBackBtnEnabled;
	}
	
	public void setToFrontBtnEnabled (boolean toFrontBtnEnabled) {
		propertyChangeSupport.firePropertyChange("toFrontBtn", this.toFrontBtnEnabled, toFrontBtnEnabled);
		this.toFrontBtnEnabled = toFrontBtnEnabled;
	}
	
	public void setToBackBtnEnabled (boolean toBackBtnEnabled) {
		propertyChangeSupport.firePropertyChange("toBackBtn", this.toBackBtnEnabled, toBackBtnEnabled);
		this.toBackBtnEnabled = toBackBtnEnabled;
				
	}

}
