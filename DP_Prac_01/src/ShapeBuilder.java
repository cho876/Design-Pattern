
import java.awt.event.MouseEvent;

public interface ShapeBuilder extends Observable{       // ShapeBuilder�� Observable�� ��ӹް� �����μ� ShapeBuilder�� ������ ��� Ŭ������ 
                                                        // Observable�ν��� ������ �ϰ� ����
	void performMousePressed(MouseEvent e);
	void performMouseReleased(MouseEvent e);
	void performMouseDragged(MouseEvent e);
}
