/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.core;

import hufs.cse.grimpan.state.*;
import hufs.cse.grimpan.state.LineBuilderState;
import hufs.cse.grimpan.state.MoveBuilderState;
import hufs.cse.grimpan.state.OvalBuilderState;
import hufs.cse.grimpan.state.PencilBuilderState;
import hufs.cse.grimpan.state.PolygonBuilderState;
import hufs.cse.grimpan.state.RegularBuilderState;
import hufs.cse.grimpan.util.PropertyManager;
import hufs.cse.grimpan.util.Util;
import hufs.cse.grimpan.command.Command;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.Stack;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GrimPanModel {
	
	private volatile static GrimPanModel uniqueModelInstance;
	
	public ObserverClass observerClass;
	private GrimPanFrameView frameView = null;
	private GrimPanController controller = null;
	
	private String defaultDir = "C:/home/cskim/temp/";
	
	public EditState editState = null;
	public EditState savedAddState = null;
	public final EditState STATE_REGULAR = new RegularBuilderState(this);
	public final EditState STATE_OVAL = new OvalBuilderState(this);
	public final EditState STATE_POLYGON = new PolygonBuilderState(this);
	public final EditState STATE_LINE = new LineBuilderState(this);
	public final EditState STATE_PENCIL = new PencilBuilderState(this);
	public final EditState STATE_MOVE = new MoveBuilderState(this);
	public final EditState STATE_DELETE = new DeleteBuilderState(this);

	//public EditState sb = null;
	
	private float shapeStrokeWidth = 1f;
	private Color shapeStrokeColor = null;
	private boolean shapeFill = false;
	private Color shapeFillColor = null;
	
	public ArrayList<GrimShape> shapeList = null;
	
	private Point mousePosition = null;
	private Point clickedMousePosition = null;
	private Point lastMousePosition = null;
	
	public Shape curDrawShape = null;
	public ArrayList<Point> polygonPoints = null;
	private int selectedShape = -1;
	private GrimShape savedPositionShape = null;
	
	private int nPolygon = 3;
	
	private File saveFile = null;
	private File recoverFile = null;
	public Stack<Command> undoCommandStack = null;
	
	public PropertyManager grimpanPM = null;
	public ClassPathXmlApplicationContext context = null;

    public Locale locale = Locale.KOREA;                         // 한/영 변환 -> 한국어
	//public Locale locale = Locale.US;                          // 한/영 변환 -> 영어
	
	private GrimPanModel(){
		this.shapeList = new ArrayList<GrimShape>();
		this.polygonPoints = new ArrayList<Point>();
		this.recoverFile = new File(defaultDir+"noname.rcv");
		this.undoCommandStack = new Stack<Command>();
		this.editState = STATE_LINE;
		this.savedAddState = this.editState;
		
	    context = new ClassPathXmlApplicationContext("application-context.xml");

		//this.grimpanPM = context.getBean("simplePropertyManager", PropertyManager.class);
		this.grimpanPM = context.getBean("xmlPropertyManager", PropertyManager.class);
		this.shapeStrokeWidth = Float.parseFloat(grimpanPM.getPanProperties().getProperty("default.stroke.width"));
		this.shapeStrokeColor = new Color(Integer.parseInt(grimpanPM.getPanProperties().getProperty("default.stroke.color"),16));
		
		observerClass = new ObserverClass(STATE_REGULAR);        // SHAPE_BUILDERS에 들어있는 모든 클래스들이 Observable로써 
		observerClass = new ObserverClass(STATE_OVAL);       // Observer를 구현한 ObserverClass를 등록시킴
		observerClass = new ObserverClass(STATE_POLYGON);
		observerClass = new ObserverClass(STATE_LINE);
		observerClass = new ObserverClass(STATE_PENCIL);
		observerClass = new ObserverClass(STATE_MOVE);
		observerClass = new ObserverClass(STATE_DELETE);
	}
	public static GrimPanModel getInstance() {
		if (uniqueModelInstance == null) {
			synchronized (GrimPanModel.class) {
				if (uniqueModelInstance == null) {
					uniqueModelInstance = new GrimPanModel();
				}
			}
		}
		return uniqueModelInstance;
	}

	/**
	 * @return the mainFrame
	 */
	public GrimPanFrameView getFrameView() {
		return frameView;
	}
	/**
	 * @param mainFrame the mainFrame to set
	 */
	public void setFrameView(GrimPanFrameView mainFrame) {
		this.frameView = mainFrame;
	}
	public EditState getEditState() {
		return editState;
	}

	public void setEditState(EditState editState) {
		this.editState = editState;
		String modeText = context.getMessage("menu.mode", null, this.locale);   // 메인 하단 부분 "모드:" (한/영 변환)
		String shapeText = context.getMessage("menu.shape", null, this.locale);  // 메인 하단 부분 "모양:" (한/영 변환)
		if (editState.getStateType() == Util.EDIT_MOVE){
			frameView.modeLBL.setText(modeText+String.format(": %s  ", context.getMessage("menu.label.move", null, this.locale)));
		}
		else if(editState.getStateType() == Util.EDIT_DELETE){
			frameView.modeLBL.setText(modeText+String.format(": %s  ",context.getMessage("menu.label.delete", null, this.locale)));
		}
		else {
			this.savedAddState = editState;
			frameView.modeLBL.setText(modeText+String.format(": %s  ", context.getMessage("menu.label.add", null, this.locale)));
			frameView.shapeLbl.setText(shapeText+String.format(": %s  ", getShapeText(editState.getStateType())));
		}
		//this.sb = editState;
	}

	public String getShapeText(int index){
		if(index == 0)
			return context.getMessage("menu.label.regular", null, this.locale);  // 정다각형
		else if(index == 1)
			return context.getMessage("menu.label.oval", null, this.locale);  // 타원
		else if(index == 2)
			return context.getMessage("menu.label.polygon", null, this.locale);  // 다각형
		else if(index == 3)
			return context.getMessage("menu.label.line", null, this.locale);  // 직선
		else
			return context.getMessage("menu.label.pencil", null, this.locale);  // 연필
	}
	
	public Point getMousePosition() {
		return mousePosition;
	}

	public void setMousePosition(Point mousePosition) {
		this.mousePosition = mousePosition;
	}
	public Point getLastMousePosition() {
		return lastMousePosition;
	}

	public void setLastMousePosition(Point mousePosition) {
		this.lastMousePosition = mousePosition;
	}

	public Point getClickedMousePosition() {
		return clickedMousePosition;
	}

	public void setClickedMousePosition(Point clickedMousePosition) {
		this.clickedMousePosition = clickedMousePosition;
	}
	/**
	 * @return the saveFile
	 */
	public File getSaveFile() {
		return saveFile;
	}

	/**
	 * @param saveFile the saveFile to set
	 */
	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
		frameView.setTitle(context.getMessage("title.label", null, this.locale)+" - "+saveFile.getPath());
	}
	/**
	 * @return the nPolygon
	 */
	public int getNPolygon() {
		return nPolygon;
	}

	/**
	 * @param nPolygon the nPolygon to set
	 */
	public void setNPolygon(int nPolygon) {
		this.nPolygon = nPolygon;
	}

	/**
	 * @return the selectedShape
	 */
	public int getSelectedShape() {
		return selectedShape;
	}

	/**
	 * @param selectedShape the selectedShape to set
	 */
	public void setSelectedShape(int selectedShape) {
		this.selectedShape = selectedShape;
	}

	/**
	 * @return the shapeStrokeColor
	 */
	public Color getShapeStrokeColor() {
		return shapeStrokeColor;
	}

	/**
	 * @param shapeStrokeColor the shapeStrokeColor to set
	 */
	public void setShapeStrokeColor(Color shapeStrokeColor) {
		this.shapeStrokeColor = shapeStrokeColor;
	}

	/**
	 * @return the shapeFill
	 */
	public boolean isShapeFill() {
		return shapeFill;
	}

	/**
	 * @param shapeFill the shapeFill to set
	 */
	public void setShapeFill(boolean shapeFill) {
		this.shapeFill = shapeFill;
	}

	/**
	 * @return the shapeFillColor
	 */
	public Color getShapeFillColor() {
		return shapeFillColor;
	}

	/**
	 * @param shapeFillColor the shapeFillColor to set
	 */
	public void setShapeFillColor(Color shapeFillColor) {
		this.shapeFillColor = shapeFillColor;
	}

	/**
	 * @return the shapeStrokeWidth
	 */
	public float getShapeStrokeWidth() {
		return shapeStrokeWidth;
	}

	/**
	 * @param shapeStrokeWidth the shapeStrokeWidth to set
	 */
	public void setShapeStrokeWidth(float shapeStrokeWidth) {
		this.shapeStrokeWidth = shapeStrokeWidth;
	}
	/**
	 * @return the defaultDir
	 */
	public String getDefaultDir() {
		return defaultDir;
	}
	/**
	 * @param defaultDir the defaultDir to set
	 */
	public void setDefaultDir(String defaultDir) {
		this.defaultDir = defaultDir;
	}
	/**
	 * @return the controller
	 */
	public GrimPanController getController() {
		return controller;
	}
	/**
	 * @param controller the controller to set
	 */
	public void setController(GrimPanController controller) {
		this.controller = controller;
	}
	/**
	 * @return the recoverFile
	 */
	public File getRecoverFile() {
		return recoverFile;
	}
	/**
	 * @param recoverFile the recoverFile to set
	 */
	public void setRecoverFile(File recoverFile) {
		this.recoverFile = recoverFile;
	}
	/**
	 * @param grimShape
	 */
	public void setSavedPositionShape(GrimShape grimShape) {
		savedPositionShape = grimShape.clone();		
	}
	public GrimShape getSavedPositionShape() {
		return savedPositionShape;		
	}

	
}
