
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MoveShapeBuilder implements ShapeBuilder {

	GrimPanModel model = null;
	private ArrayList observerList;

	public MoveShapeBuilder(GrimPanModel model){
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
			observer.update(0);                             // Move(이동)에는 count에 변동이 없어야 하므로 0을 넘김 (count += 0)
		}
	}
	
	@Override
	public void performMousePressed(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		model.setClickedMousePosition(p1);

		getSelectedShape();
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseReleased(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);

		endShapeMove();
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void performMouseDragged(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setLastMousePosition(model.getMousePosition());
		model.setMousePosition(p1);

		moveShapeByMouse();
	}
	private void getSelectedShape(){
		int selIndex = -1;
		IGrimShape shape = null;
		for (int i=model.shapeList.size()-1; i >= 0; --i){
			shape = (IGrimShape)model.shapeList.get(i);
			if (shape.contains(model.getMousePosition().getX(), model.getMousePosition().getY())){
				selIndex = i;
				break;
			}
		}
		if (selIndex != -1){
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
		model.setSelectedShape(selIndex);
	}
	private void moveShapeByMouse(){
		int selIndex = model.getSelectedShape();
		IGrimShape shape = null;
		if (selIndex != -1){
			shape = (IGrimShape)model.shapeList.get(selIndex);
			shape.translate(
					(float)(model.getMousePosition().x-model.getLastMousePosition().x), 
					(float)(model.getMousePosition().y-model.getLastMousePosition().y));
		}
	}
	private void endShapeMove(){
		int selIndex = model.getSelectedShape();
		IGrimShape shape = null;
		if (selIndex != -1){
			shape = (IGrimShape)model.shapeList.get(selIndex);
			Color scolor = shape.getStrokeColor();
			if (scolor!=null) {
				shape.setStrokeColor(new Color (scolor.getRed(), scolor.getGreen(), scolor.getBlue(), 255));
			}
			if (shape.getGrimPaint() instanceof Color) {
				Color fcolor = (Color)shape.getGrimPaint();
				shape.setGrimPaint(new Color (fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue(), 255));
			}
		}
	}

}
