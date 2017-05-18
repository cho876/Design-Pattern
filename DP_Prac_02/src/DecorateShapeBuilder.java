

import java.awt.Color;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DecorateShapeBuilder implements ShapeBuilder {

	GrimPanModel model = null;

	public DecorateShapeBuilder(GrimPanModel model){
		this.model = model;
	}

	@Override
	public void RegisterObserver(Observer observer)
	{
		// not need Observer
	}
	
	@Override
	public void RemoveObserver(Observer observer)
	{
		// not need Observer
	}
	
	@Override
	public void NotifyObserver()
	{
		// not need Observer
	}
	
	@Override
	public void performMousePressed(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		model.setClickedMousePosition(p1);

		getSelectedShape();
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
				System.out.println("2");
				Color fcolor = (Color)shape.getGrimPaint();
				shape.setGrimPaint(new Color (fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue(), 127));
			}
		}
		model.setSelectedShape(selIndex);
	}

	@Override
	public void performMouseReleased(MouseEvent e) {
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
			if (model.getEditState() == Util.DECO_GLASS) {
				model.shapeList.set(selIndex, new GlassGrimShape(shape));

			}
			else if (model.getEditState() == Util.DECO_TEX) {
				model.shapeList.set(selIndex, new TextureGrimShape(shape));


			}
			else if (model.getEditState() == Util.DECO_BALL) {
				model.shapeList.set(selIndex, new BallGrimShape(shape));


			}
			else if (model.getEditState() == Util.DECO_TRANS) {
				    // shapeList에 저장되어 있던 grimShape을 TransparentGrimShape의 매개변수로 담는다.
					model.shapeList.set(selIndex, new TransparentGrimShape(model.shapeList.get(selIndex)));
			}
		}
	}

	@Override
	public void performMouseDragged(MouseEvent e) {

	}

}
