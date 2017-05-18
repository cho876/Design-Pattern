

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

public abstract class DecorateGrimShape implements IGrimShape {

	IGrimShape grimShape = null;
	
	public DecorateGrimShape(IGrimShape grimShape) {
		this.grimShape = grimShape;
	}
	@Override
	public abstract void draw(Graphics2D g2);
	@Override
	public void translate(float dx, float dy) {
		this.grimShape.translate(dx, dy);
	}
	@Override
	public boolean contains(double px, double py) {
		return this.grimShape.contains(px, py);
	}
	@Override
	public Shape getShape() {
		return this.grimShape.getShape();
	}
	@Override
	public void setShape(Shape gpShape) {
		this.grimShape.setShape(gpShape);
		
	}
	@Override
	public float getStrokeWidth() {
		return this.grimShape.getStrokeWidth();
	}
	@Override
	public void setStrokeWidth(float strokeWidth) {
		this.grimShape.setStrokeWidth(strokeWidth);
		
	}
	@Override
	public Color getStrokeColor() {
		return this.grimShape.getStrokeColor();
	}
	@Override
	public void setStrokeColor(Color strokeColor) {
		this.grimShape.setStrokeColor(strokeColor);
		
	}
	@Override
	public Paint getGrimPaint() {
		return this.grimShape.getGrimPaint();
	}
	@Override
	public void setGrimPaint(Paint grimPaint) {
		this.grimShape.setGrimPaint(grimPaint);	
	}
	
}
