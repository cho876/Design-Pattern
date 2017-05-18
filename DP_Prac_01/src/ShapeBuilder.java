/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */


import java.awt.event.MouseEvent;

/**
 * @author cskim
 *
 */
public interface ShapeBuilder extends Observable{       // ShapeBuilder가 Observable을 상속받게 함으로서 ShapeBuilder를 구현한 모든 클래스가 
                                                        // Observable로써의 역할을 하게 만듬
	void performMousePressed(MouseEvent e);
	void performMouseReleased(MouseEvent e);
	void performMouseDragged(MouseEvent e);
}
