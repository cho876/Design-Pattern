

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

public class BallGrimShape extends DecorateGrimShape {

	Paint paint = null;
	
	public BallGrimShape(IGrimShape grimShape) {
		super(grimShape);
		paint = TexturePaintFactory.createBallPaint(grimShape.getShape());
	}
	@Override
	public void draw(Graphics2D g2) {
		g2.setPaint(paint);
		g2.fill(this.getShape());
	}
	@Override
	public void translate(float dx, float dy) {
		this.grimShape.translate(dx, dy);
		this.paint = TexturePaintFactory.createBallPaint(grimShape.getShape());
	}
}
