/**
 * Created on 2015. 4. 28.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.state;

import java.awt.event.MouseEvent;

/**
 * @author cskim
 *
 */
public interface EditState extends Observable  {

	public void performMousePressed(MouseEvent e);
	public void performMouseReleased(MouseEvent e);
	public void performMouseDragged(MouseEvent e);
	public int getStateType();
}
