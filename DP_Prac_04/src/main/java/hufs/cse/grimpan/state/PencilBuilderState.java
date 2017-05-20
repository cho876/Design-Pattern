/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.state;

import hufs.cse.grimpan.core.GrimPanController;
import hufs.cse.grimpan.core.GrimPanModel;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * @author cskim
 *
 */
public class PencilBuilderState implements EditState {

	public static final int STATE_TYPE = 4;
	GrimPanModel model = null;
	private ArrayList observerList;

	public PencilBuilderState(GrimPanModel model){
		this.model = model;
		observerList = new ArrayList();
	}
	@Override
	public int getStateType() {
		return STATE_TYPE;
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
			observer.update(1);                       // update함수에 1을 넘겨줌으로써 count에 +1 추가 (count += 1)
		}
	}
	

	public void countChange()
	{
		NotifyObserver();
	}
	
	
	@Override
	public void performMousePressed(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		model.setClickedMousePosition(p1);
		model.setLastMousePosition(model.getMousePosition());				
		model.curDrawShape = new Path2D.Double();
		((Path2D)model.curDrawShape).moveTo((double)p1.x, (double)p1.y);
		countChange();
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseReleased(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		((Path2D)model.curDrawShape).lineTo((double)p1.x, (double)p1.y);
		model.getController().addShapeAction();

	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseDragged(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setLastMousePosition(model.getMousePosition());
		model.setMousePosition(p1);
		((Path2D)model.curDrawShape).lineTo((double)p1.x, (double)p1.y);

	}

}
