package hufs.ces.grimpan.command;

import hufs.ces.grimpan.core.GrimPanFrameView;
import hufs.ces.grimpan.core.GrimPanModel;
import hufs.ces.grimpan.shape.GrimShape;
import hufs.ces.grimpan.shape.ObserverClass;

public class DelCommand implements Command {
	GrimPanModel model = null;
	GrimShape savedGrimShape = null;  // ������ ������ ������� ����
	int selIndex = 0;
	int index = 0;
	
	public DelCommand(GrimPanModel model, GrimShape grimShape){  
		this.model = model;
		this.savedGrimShape = grimShape;  // ������ ���� ����
	}

	@Override
	public void execute() {
	}

	@Override
	public void undo() {
		model.shapeList.add(savedGrimShape);     // shapeList�� size ���� 
		update();
		
		// target_selIndex = shapeList�� �ֻ��� index
		int target_selIndex = model.shapeList.indexOf(model.shapeList.get(model.shapeList.size()-1));  
		if (target_selIndex != -1){
			// shapeList[targget_selIndex]�� ������ �������� savedGrimShape(������ ����)�� ��ġ�� ����
			model.shapeList.set(target_selIndex, savedGrimShape);  
		}
	}
	
	public void update(){   // undo�� ��� ������ �ٽ� �����ǹǷ� count�� ������Ű�� ����ϴ� �Լ�
		++ObserverClass.count;
		String text = "Count: "+ObserverClass.count;
		GrimPanFrameView.countLbl.setText(text);           // ��ȭ�� count���� �������� countLbl�� ���� ��ȭ��Ŵ (Count: count(static ����))
	}
}
