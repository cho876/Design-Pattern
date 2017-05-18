package hufs.ces.grimpan.command;

import hufs.ces.grimpan.core.GrimPanFrameView;
import hufs.ces.grimpan.core.GrimPanModel;
import hufs.ces.grimpan.shape.GrimShape;
import hufs.ces.grimpan.shape.ObserverClass;

public class DelCommand implements Command {
	GrimPanModel model = null;
	GrimShape savedGrimShape = null;  // 삭제된 도형을 담기위한 변수
	int selIndex = 0;
	int index = 0;
	
	public DelCommand(GrimPanModel model, GrimShape grimShape){  
		this.model = model;
		this.savedGrimShape = grimShape;  // 삭제된 도형 저장
	}

	@Override
	public void execute() {
	}

	@Override
	public void undo() {
		model.shapeList.add(savedGrimShape);     // shapeList의 size 증가 
		update();
		
		// target_selIndex = shapeList의 최상위 index
		int target_selIndex = model.shapeList.indexOf(model.shapeList.get(model.shapeList.size()-1));  
		if (target_selIndex != -1){
			// shapeList[targget_selIndex]의 도형을 생성자의 savedGrimShape(삭제된 도형)의 위치로 변경
			model.shapeList.set(target_selIndex, savedGrimShape);  
		}
	}
	
	public void update(){   // undo할 경우 도형이 다시 생성되므로 count가 증가시키고 출력하는 함수
		++ObserverClass.count;
		String text = "Count: "+ObserverClass.count;
		GrimPanFrameView.countLbl.setText(text);           // 변화된 count값을 바탕으로 countLbl의 값을 변화시킴 (Count: count(static 변수))
	}
}
