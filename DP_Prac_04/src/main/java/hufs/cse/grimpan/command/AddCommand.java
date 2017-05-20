/**
 * Created on 2015. 4. 4.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.command;

import hufs.cse.grimpan.core.*;
import hufs.cse.grimpan.state.*;

public class AddCommand implements Command {

	GrimPanModel model = null;
	GrimShape grimShape = null;
	public AddCommand(GrimPanModel model, GrimShape grimShape){
		this.model = model;
		this.grimShape = grimShape;
	}
	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.command.Command#execute()
	 */
	@Override
	public void execute() {
		if (model.curDrawShape != null){
			model.shapeList.add(grimShape);
			model.curDrawShape = null;
		}
	}

	@Override
	public void undo() {
		update();
		model.shapeList.remove(model.shapeList.size()-1);
	}
	
	public void update(){
		--ObserverClass.count;
		String text = ""+ObserverClass.count;
		GrimPanFrameView.countNumLbl.setText(text);           // 변화된 count값을 바탕으로 countNumLbl의 값을 변화시킴 
	}

}
