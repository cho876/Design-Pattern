package hufs.ces.grimpan.command;

import hufs.ces.grimpan.core.GrimPanFrameView;
import hufs.ces.grimpan.core.GrimPanModel;
import hufs.ces.grimpan.shape.GrimShape;
import hufs.ces.grimpan.shape.ObserverClass;

public class AddCommand implements Command {
	GrimPanModel model = null;
	GrimShape grimShape = null;
	public AddCommand(GrimPanModel model, GrimShape grimShape){
		this.model = model;
		this.grimShape = grimShape;

	}
	
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
		String text = "Count: "+ObserverClass.count;
		GrimPanFrameView.countLbl.setText(text);           // ��ȭ�� count���� �������� countLbl�� ���� ��ȭ��Ŵ (Count: count(static ����))
	}
}
