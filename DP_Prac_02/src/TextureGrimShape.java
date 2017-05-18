
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

public class TextureGrimShape extends DecorateGrimShape {

	Paint paint = null;
	public TextureGrimShape(IGrimShape grimShape) {
		super(grimShape);
		paint = TexturePaintFactory.createTexturePaint("/images/color-brick-wall-50x30.jpg");
	}
	@Override
	public void draw(Graphics2D g2) {
		g2.setPaint(paint);
		g2.fill(this.getShape());
	}
}
