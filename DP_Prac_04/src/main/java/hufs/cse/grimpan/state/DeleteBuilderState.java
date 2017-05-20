package hufs.cse.grimpan.state;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import hufs.cse.grimpan.core.*;

public class DeleteBuilderState extends Frame implements EditState{

	ClassPathXmlApplicationContext ctx;

	public static final int STATE_TYPE = 6;
	GrimPanModel model = null;
	private ArrayList observerList;

	public DeleteBuilderState(GrimPanModel model){
		this.model = model;
		observerList = new ArrayList();
	}
	
	@Override
	public void RegisterObserver(Observer observer)  // Observer 등록
	{
		observerList.add(observer);
	}
	
	@Override
	public void RemoveObserver(Observer observer)  // Observer 삭제
	{
		int index = observerList.indexOf(observer);
		if(index>=0)
			observerList.remove(index);
	}
	
	@Override
	public void NotifyObserver()               // Observer에게 알림
	{
		for(int i=0; i<observerList.size();i++)
		{
			Observer observer = (Observer)observerList.get(i);
			observer.update(-1);  // 삭제 시, count를 -1만큼 줄이기 위해 update함수 호출
		}
	}
	
	public void countChange()   // count 변경 요청 시, NotifyObserver함수 호출
	{
		NotifyObserver();
	}
	
	@Override
	public int getStateType() {
		return STATE_TYPE;
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
			if (shape.contains(model.getMousePosition().getX(), model.getMousePosition().getY())){    // 마우스 좌표가 도형의 범주 내에 들어왔을 시
				{
					selIndex = i;
					
					int delCheck = JOptionPane.showConfirmDialog(null, "정말 지우시겠습니까?",        // 팝업창 구현
							"Delete Tile", JOptionPane.OK_CANCEL_OPTION);
					if (delCheck == JOptionPane.OK_OPTION){                  // OK버튼 시, selIndex에 있는 도형 삭제
						model.getController().deleteShapeAction(selIndex);	 // DelCommand를 실행하기 위한 부분
						model.shapeList.remove(selIndex);
						selIndex = -1;                                 // 인덱스 다시 초기화
						repaint();
					}
					countChange();
				}
				break;
			}
		}
		if (selIndex != -1){
			model.setLastMousePosition(model.getClickedMousePosition());
			Color scolor = shape.getGrimStrokeColor();
			shape.setGrimStrokeColor(new Color (scolor.getRed(), scolor.getGreen(), scolor.getBlue(), 127));		
		}
		model.setSelectedShape(selIndex);
	}
}
