
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

public class TransparentGrimShape extends DecorateGrimShape {
	
	Color tcolor = null;
    Paint paint;
	public TransparentGrimShape(IGrimShape grimShape) {     
		super(grimShape);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		grimShape.draw(g2);                        // 생성자에서 받아온 IGrimShape에 맞춰 그린다.
		g2.setColor(new Color(240,240,240,20));    // 그 위에 색을 덮어씌워 투명도를 조절한다.
		g2.fill(this.getShape());
	}
}