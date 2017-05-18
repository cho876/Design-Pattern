/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */


import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * @author cskim
 *
 */
public class RegularShapeBuilder implements ShapeBuilder {

	GrimPanModel model = null;
	private ArrayList observerList;
	
	public RegularShapeBuilder(GrimPanModel model){
		this.model = model;
		observerList = new ArrayList();
	}
	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMousePressed(java.awt.event.MouseEvent)
	 */
	
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
			observer.update(1);                           // update함수에 1을 넘겨줌으로써 count에 +1 추가 (count += 1)
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

		genCenteredPolygon();
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseReleased(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);

		genCenteredPolygon();
		if (model.curDrawShape != null){
			model.shapeList
			.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
					model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
			model.curDrawShape = null;
		}
		countChange();
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseDragged(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setLastMousePosition(model.getMousePosition());
		model.setMousePosition(p1);

		genCenteredPolygon();
	}

	private void genCenteredPolygon(){
		Point pi = model.getMousePosition();
		Point center = model.getClickedMousePosition();
		if (pi.distance(new Point2D.Double(center.x, center.y)) <= Util.MINPOLYDIST)
			return;

		getPolygonWithCenterNVertex(center, new Point2D.Double(pi.x, pi.y));
	}
	private void getPolygonWithCenterNVertex(Point center, Point2D pi){
		AffineTransform rotNTimes = new AffineTransform();
		rotNTimes.setToRotation(2*Math.PI/model.getNPolygon()); //360/n degree

		Point2D[] polyPoints = new Point2D[model.getNPolygon()];
		polyPoints[0] = new Point2D.Double(pi.getX()-center.x, pi.getY()-center.y); 
		for (int i=1; i<polyPoints.length; ++i){
			polyPoints[i] = (Point2D)polyPoints[i-1].clone();
			rotNTimes.transform(polyPoints[i], polyPoints[i]);
		}

		polyPoints[0] = new Point2D.Double(pi.getX(), pi.getY()); 
		for (int i=1; i<polyPoints.length; ++i){
			polyPoints[i].setLocation(
					polyPoints[i].getX()+center.x, 
					polyPoints[i].getY()+center.y);
		}
		Path2D polygonPath = new Path2D.Double();
		polygonPath.moveTo(polyPoints[0].getX(), polyPoints[0].getY());
		//System.out.println("x= "+polyPoints[0].getX()+" y= "+polyPoints[0].getY());
		for (int i=1; i<polyPoints.length; ++i){
			polygonPath.lineTo(polyPoints[i].getX(), polyPoints[i].getY());
			//System.out.println("x= "+polyPoints[i].getX()+" y= "+polyPoints[i].getY());
		}
		polygonPath.closePath();

		model.curDrawShape = polygonPath;
		
	}
}
