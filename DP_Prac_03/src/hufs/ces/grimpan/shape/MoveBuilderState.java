/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.ces.grimpan.shape;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import hufs.ces.grimpan.core.GrimPanController;
import hufs.ces.grimpan.core.GrimPanModel;

/**
 * @author cskim
 *
 */
public class MoveBuilderState implements EditState {

	GrimPanModel model = null;
	private ArrayList observerList;

	public MoveBuilderState(GrimPanModel model){
		this.model = model;
		observerList = new ArrayList();
	}
	

	@Override
	public void RegisterObserver(Observer observer)
	{
		observerList.add(observer);
	}
	
	@Override
	public void RemoveObserver(Observer observer)
	{
		int index = observerList.indexOf(observer);
		if(index>=0)
			observerList.remove(index);
	}
	
	@Override
	public void NotifyObserver()
	{
		for(int i=0; i<observerList.size();i++)
		{
			Observer observer = (Observer)observerList.get(i);
			observer.update(0);                             // Move(�̵�)���� count�� ������ ����� �ϹǷ� 0�� �ѱ� (count += 0)
		}
	}
	
	@Override
	public int getStateType() {
		return EditState.EDIT_MOVE;
	}
	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMousePressed(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		model.setClickedMousePosition(p1);

		int selIndex = -1;
		GrimShape shape = null;
		for (int i=model.shapeList.size()-1; i >= 0; --i){
			shape = model.shapeList.get(i);
			if (shape.contains(model.getMousePosition().getX(), model.getMousePosition().getY())){
				selIndex = i;
				break;
			}
		}
		if (selIndex != -1){
			model.setSavedPositionShape(model.shapeList.get(selIndex));
			model.setLastMousePosition(model.getClickedMousePosition());
			Color scolor = shape.getStrokeColor();
			if (scolor!=null) {
				shape.setStrokeColor(new Color (scolor.getRed(), scolor.getGreen(), scolor.getBlue(), 127));
			}
			if (shape.getGrimPaint() instanceof Color) {
				Color fcolor = (Color)shape.getGrimPaint();
				shape.setGrimPaint(new Color (fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue(), 127));
			}
		}
		model.setSelectedShapeIndex(selIndex);
	}
	@Override
	public void performMouseDragged(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setLastMousePosition(model.getMousePosition());
		model.setMousePosition(p1);

		int selIndex = model.getSelectedShapeIndex();
		GrimShape shape = null;
		if (selIndex != -1){
			shape = model.shapeList.get(selIndex);
			shape.translate(
					(float)(model.getMousePosition().x-model.getLastMousePosition().x), 
					(float)(model.getMousePosition().y-model.getLastMousePosition().y));
		}
	}
	@Override
	public void performMouseReleased(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);

		endShapeMove();
	}

	private void endShapeMove(){
		int selIndex = model.getSelectedShapeIndex();
		GrimShape shape = null;
		if (selIndex != -1){
			shape = model.shapeList.get(selIndex);
			Color scolor = shape.getStrokeColor();
			if (scolor!=null) {
				shape.setStrokeColor(new Color (scolor.getRed(), scolor.getGreen(), scolor.getBlue(), 255));
			}
			if (shape.getGrimPaint() instanceof Color) {
				Color fcolor = (Color)shape.getGrimPaint();
				shape.setGrimPaint(new Color (fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue(), 255));
			}
			model.getController().moveShapeAction();
		}
	}

}
