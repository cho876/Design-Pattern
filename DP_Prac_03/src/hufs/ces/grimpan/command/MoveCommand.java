package hufs.ces.grimpan.command;

import hufs.ces.grimpan.core.GrimPanModel;
import hufs.ces.grimpan.shape.GrimShape;

public class MoveCommand implements Command {

	GrimPanModel model = null;
	GrimShape savedGrimShape = null;
	GrimShape movedGrimShape = null;
	public MoveCommand(GrimPanModel model, GrimShape grimShape){
		this.model = model;
		this.savedGrimShape = grimShape;
	}
	
	@Override
	public void execute() {
		movedGrimShape = model.shapeList.get(model.getSelectedShapeIndex());
	}
	
	@Override
	public void undo() {
		int selIndex = model.shapeList.indexOf(movedGrimShape);
		if (selIndex != -1){
			model.shapeList.set(selIndex, savedGrimShape);
		}
	}

}
