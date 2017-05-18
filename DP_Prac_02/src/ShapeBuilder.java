
import java.awt.event.MouseEvent;


public interface ShapeBuilder extends Observable{

	void performMousePressed(MouseEvent e);
	void performMouseReleased(MouseEvent e);
	void performMouseDragged(MouseEvent e);
}
