
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class PolygonShapeBuilder implements ShapeBuilder {

	GrimPanModel model = null;
	private ArrayList observerList;

	public PolygonShapeBuilder(GrimPanModel model){
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
			observer.update(1);                            // update함수에 1을 넘겨줌으로써 count에 +1 추가 (count += 1)
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

		model.polygonPoints.add(p1);
		model.curDrawShape = Util.buildPath2DFromPoints(model.polygonPoints);
		if (e.isShiftDown()) {
			((Path2D)(model.curDrawShape)).closePath();
			model.polygonPoints.clear();
			model.shapeList
			.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
					model.getShapeStrokeColor(), model.getShapeFillColor()));
			model.curDrawShape = null;
		}
		countChange();
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseReleased(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);

		model.polygonPoints.add(p1);
		model.curDrawShape = Util.buildPath2DFromPoints(model.polygonPoints);
		if (e.isShiftDown()) {
			((Path2D)(model.curDrawShape)).closePath();
			model.polygonPoints.clear();
			model.shapeList
			.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
					model.getShapeStrokeColor(), model.getShapeFillColor()));
			model.curDrawShape = null;
		}
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseDragged(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setLastMousePosition(model.getMousePosition());
		model.setMousePosition(p1);

	}

}
