

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

public interface IGrimShape {

	void draw(Graphics2D g2);

	void translate(float dx, float dy);

	boolean contains(double px, double py);

	Shape getShape();

	void setShape(Shape gpShape);

	float getStrokeWidth();

	void setStrokeWidth(float strokeWidth);

	Color getStrokeColor();

	void setStrokeColor(Color strokeColor);

	Paint getGrimPaint();

	void setGrimPaint(Paint grimPaint);

}