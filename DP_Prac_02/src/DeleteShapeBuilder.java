

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class DeleteShapeBuilder extends Frame implements ShapeBuilder{   // ���� ����� �����ϱ� ���� Ŭ����(Observable ���� Ŭ����)
	
	GrimPanModel model = null;
	private ArrayList observerList;           // Observer���� ���� ArrayList �߰�
	public DeleteShapeBuilder(GrimPanModel model){
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
		IGrimShape shape = null;
		for (int i=model.shapeList.size()-1; i >= 0; --i){
			shape = model.shapeList.get(i);
			if (shape.contains(model.getMousePosition().getX(), model.getMousePosition().getY())){    // ���콺 ��ǥ�� ������ ���� ���� ������ ��
				{
					selIndex = i;
					
					int delCheck = JOptionPane.showConfirmDialog(null, "������ ������ �����ϰڽ��ϱ�?",    // �˾�â ����
							"Delete Tile", JOptionPane.OK_CANCEL_OPTION);
					if (delCheck == JOptionPane.OK_OPTION){                  // OK��ư ��, selIndex�� �ִ� ���� ����
						model.shapeList.remove(selIndex);
						selIndex = -1;                                 // �ε��� �ٽ� �ʱ�ȭ
						repaint();
					}
					countChange();                                    // ���������Ƿ� count�� �ٿ��������� countChange�Լ� ȣ��
				}
				break;
			}
		}
		if (selIndex != -1){
			model.setLastMousePosition(model.getClickedMousePosition());
			Color scolor = shape.getStrokeColor();
			//Color fcolor = shape.getGrimFillColor();
			shape.setStrokeColor(new Color (scolor.getRed(), scolor.getGreen(), scolor.getBlue(), 127));
			//shape.setGrimFillColor(new Color (fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue(), 127));
		}
		model.setSelectedShape(selIndex);
	}
}
