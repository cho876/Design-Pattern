
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class TexturePaintFactory {
		
	public static Paint createTexturePaint(String imageFile){

		BufferedImage mImage = TexturePaintFactory.getBufferedImageFromFile(imageFile);
	    return new TexturePaint(mImage, new Rectangle2D.Double(0, 0, mImage.getWidth(), mImage.getHeight()));	    
	}
	
	public static Paint createGlassPaint(Shape shape){
		Rectangle bounds = shape.getBounds();
		int px1 = (int)(bounds.getX()+(bounds.getWidth()*35./80.));
		int py1 = (int)(bounds.getY()+(bounds.getWidth()*35./80.));
		int px2 = (int)(bounds.getX()+(bounds.getWidth()*55./80.));
		int py2 = (int)(bounds.getY()+(bounds.getWidth()*55./80.));
		return new GradientPaint(px1, py1, Color.white, px2, py2, new Color(102, 178, 255, 255), true);
	}

	public static Paint createBallPaint(Shape shape){
		Rectangle bounds = shape.getBounds();
		float radius = Math.max((float)bounds.getWidth()/2f, (float)bounds.getHeight()/2f);
		Point2D center = new Point2D.Float(
				(float)bounds.getX()+(float)bounds.getWidth()/2f, 
				(float)bounds.getY()+(float)bounds.getHeight()/2f);
		float[] dist = { 0f, 0.2f, 1.0f };
		Color[] colors = { new Color(255, 229, 204), new Color(255, 128, 0), new Color(102, 0, 0) };
		return new RadialGradientPaint(center, radius, dist, colors);
	}

	
	public static BufferedImage getBufferedImageFromFile(String imageFile){		
		// build bufferedImage from image file
		BufferedImage buffImage = null;
	    try {
	    	InputStream in = TexturePaintFactory.class.getResourceAsStream(imageFile);
			buffImage = ImageIO.read(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    //System.out.println("Buff Image w="+buffImage.getWidth()+" h="+buffImage.getHeight());
	    return buffImage;
	}

}
