package command;

import geometry.Shape;
import mvc.DrawingModel;

public class CmdRemoveShape implements Command{
	
	private Shape shape;
	
	private DrawingModel model;
	
	private int index;
	
	public CmdRemoveShape(DrawingModel model, Shape shape, int index) {
		this.model = model;
		this.shape = shape;
		this.index = index;
		
	}

	@Override
	public void execute() {
		model.remove(shape);
		
	}

	@Override
	public void unexecute() {
		model.getShapes().add(index, shape);
		
	}
	
	@Override
	public String toString() {
		return "Removed - " + this.shape + "\n";
	}

}
