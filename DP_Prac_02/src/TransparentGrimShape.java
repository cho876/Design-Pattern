
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
		grimShape.draw(g2);                        // �����ڿ��� �޾ƿ� IGrimShape�� ���� �׸���.
		g2.setColor(new Color(240,240,240,20));    // �� ���� ���� ����� ������ �����Ѵ�.
		g2.fill(this.getShape());
	}
}