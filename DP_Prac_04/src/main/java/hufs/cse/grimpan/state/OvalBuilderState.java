/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.state;

import hufs.cse.grimpan.core.GrimPanController;
import hufs.cse.grimpan.core.GrimPanModel;
import hufs.cse.grimpan.util.Util;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * @author cskim
 *
 */
public class OvalBuilderState implements EditState {

	public static final int STATE_TYPE = 1;
	GrimPanModel model = null;
	private ArrayList observerList;
	
	public OvalBuilderState(GrimPanModel model){
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
			observer.update(1);                        // update함수에 1을 넘겨줌으로써 count에 +1 추가 (count += 1)
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
		countChange();
		genEllipse2D();
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseReleased(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);

		genEllipse2D();
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

		genEllipse2D();
	}
	
	private void genEllipse2D(){
		Point pi = model.getMousePosition();
		Point topleft = model.getClickedMousePosition();
		if (pi.distance(new Point2D.Double(topleft.x, topleft.y)) <= Util.MINPOLYDIST)
			return;
		Ellipse2D oval = new Ellipse2D.Double(
				topleft.x, topleft.y,
				pi.x-topleft.x, pi.y-topleft.y);
		model.curDrawShape = oval;
	}

}
