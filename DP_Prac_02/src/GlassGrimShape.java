import java.awt.Graphics2D;
import java.awt.Paint;

public class GlassGrimShape extends DecorateGrimShape {

	Paint paint = null;
	public GlassGrimShape(IGrimShape grimShape) {
		super(grimShape);
		paint = TexturePaintFactory.createGlassPaint(grimShape.getShape());
		}
	@Override
	public void draw(Graphics2D g2) {
		g2.setPaint(paint);
		g2.fill(this.getShape());
	}
}
