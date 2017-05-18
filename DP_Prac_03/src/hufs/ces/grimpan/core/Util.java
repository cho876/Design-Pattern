
package hufs.ces.grimpan.core;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class Util {

	public static final int MINPOLYDIST = 6;
	
	public static final String[] SHAPE_NAME = {
		"���ٰ���", "Ÿ����", "�ٰ���", "����", "����", "�̵�",
	};
	
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
