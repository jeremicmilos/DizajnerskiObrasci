package command;

import geometry.Circle;

public class CmdModifyCircle implements Command{

	private Circle oldState;
	private Circle newState;
	private Circle original = new Circle();
	
	public CmdModifyCircle (Circle oldState, Circle newState) {
		this.newState = newState;
		this.oldState = oldState;
	}
	
	@Override
	public void execute() {
		original = oldState.clone(original);
		oldState = newState.clone(oldState);
		
	}

	@Override
	public void unexecute() {
		oldState = original.clone(oldState);
		
	}
	
	@Override
	public String toString() {
		return "Modified - " + this.original + " " + "->" + " " + this.newState + "\n";
	}

}
