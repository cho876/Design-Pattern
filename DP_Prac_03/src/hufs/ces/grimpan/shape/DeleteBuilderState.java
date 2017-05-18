package hufs.ces.grimpan.shape;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import hufs.ces.grimpan.core.GrimPanModel;

public class DeleteBuilderState extends Frame implements EditState{

	GrimPanModel model = null;
	private ArrayList observerList;

	public DeleteBuilderState(GrimPanModel model){
		this.model = model;
		observerList = new ArrayList();
	}
	
	@Override
	public void RegisterObserver(Observer observer)  // Observer ���
	{
		observerList.add(observer);
	}
	
	@Override
	public void RemoveObserver(Observer observer)  // Observer ����
	{
		int index = observerList.indexOf(observer);
		if(index>=0)
			observerList.remove(index);
	}
	
	@Override
	public void NotifyObserver()               // Observer���� �˸�
	{
		for(int i=0; i<observerList.size();i++)
		{
			Observer observer = (Observer)observerList.get(i);
			observer.update(-1);  // ���� ��, count�� �پ��� �ϱ� ���� -1�� �ѱ�
		}
	}
	
	public void countChange()   // count ���� ��û ��, NotifyObserver�Լ� ȣ��
	{
		NotifyObserver();
	}
	
	@Override
	public int getStateType() {
		return EditState.EDIT_DELETE;
	}
	
	@Override
	public void performMousePressed(MouseEvent e)
	{
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		model.setClickedMousePosition(p1);
		getSelectedShape(e);  
	}
	
	@Override
	public void performMouseReleased(MouseEvent e)
	{
	}
	
	@Override
	public void performMouseDragged(MouseEvent e)
	{
	}
	
	private void getSelectedShape(MouseEvent e){
		int selIndex = -1;
		GrimShape shape = null;
		for (int i=model.shapeList.size()-1; i >= 0; --i){
			shape = model.shapeList.get(i);
			if (shape.contains(model.getMousePosition().getX(), model.getMousePosition().getY())){    // ���콺 ��ǥ�� ������ ���� ���� ������ ��
				{
					selIndex = i;
					
					int delCheck = JOptionPane.showConfirmDialog(null, "������ ������ �����ϰڽ��ϱ�?",    // �˾�â ����
							"Delete Tile", JOptionPane.OK_CANCEL_OPTION);
					if (delCheck == JOptionPane.OK_OPTION){                  // OK��ư ��, selIndex�� �ִ� ���� ����
						model.getController().deleteShapeAction(selIndex);	 // DelCommand�� �����ϱ� ���� �κ�
						model.shapeList.remove(selIndex);
						selIndex = -1;                                 // �ε��� �ٽ� �ʱ�ȭ
						repaint();
					}
					countChange();
				}
				break;
			}
		}
		if (selIndex != -1){
			model.setLastMousePosition(model.getClickedMousePosition());
			Color scolor = shape.getStrokeColor();
			shape.setStrokeColor(new Color (scolor.getRed(), scolor.getGreen(), scolor.getBlue(), 127));		
		}
		model.setSelectedShapeIndex(selIndex);
	}
}
