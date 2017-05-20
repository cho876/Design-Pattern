/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.util;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import hufs.cse.grimpan.core.GrimPanModel;

/**
 * @author cskim
 *
 */
public class Util {

	public static final int MINPOLYDIST = 6;
	public static final int SHAPE_REGULAR = 0;
	public static final int SHAPE_OVAL = 1;
	public static final int SHAPE_POLYGON = 2;
	public static final int SHAPE_LINE = 3;
	public static final int SHAPE_PENCIL = 4;
	public static final int EDIT_MOVE = 5;
	public static final int EDIT_DELETE = 6;
	public static final int DECO_GLASS = 7;
	public static final int DECO_TEX = 8;
	public static final int DECO_BALL = 9;
	public static final int DECO_TRANS = 10;

	public static Path2D buildPath2DFromPoints(ArrayList<Point> points){
		Path2D result = new Path2D.Double();
		if (points != null && points.size() > 0) {
			Point p0 = points.get(0);
			result.moveTo((double)(p0.x), (double)(p0.y));
			for (int i=1; i<points.size(); ++i){
				p0 = points.get(i);
				result.lineTo((double)(p0.x), (double)(p0.y));
			}
		}
		
		return result;
	}
}
