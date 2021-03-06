package hufs.ces.grimpan.core;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import hufs.ces.grimpan.command.Command;
import hufs.ces.grimpan.shape.DeleteBuilderState;
import hufs.ces.grimpan.shape.EditState;
import hufs.ces.grimpan.shape.GrimShape;
import hufs.ces.grimpan.shape.LineBuilderState;
import hufs.ces.grimpan.shape.MoveBuilderState;
import hufs.ces.grimpan.shape.ObserverClass;
import hufs.ces.grimpan.shape.OvalBuilderState;
import hufs.ces.grimpan.shape.PencilBuilderState;
import hufs.ces.grimpan.shape.PolygonBuilderState;
import hufs.ces.grimpan.shape.RegularBuilderState;

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
	private int selectedShapeIndex = -1;
	private GrimShape savedPositionShape = null;
	
	private int nPolygon = 3;
	
	private File saveFile = null;
	private File recoverFile = null;
	public Stack<Command> undoCommandStack = null;
	
	public XMLPropertyManager grimpanPM = null;
	
	private GrimPanModel(){
		this.shapeList = new ArrayList<GrimShape>();
		this.shapeStrokeColor = Color.BLACK;
		this.shapeFillColor = Color.BLACK;
		this.polygonPoints = new ArrayList<Point>();
		this.recoverFile = new File(defaultDir+"noname.rcv");
		this.undoCommandStack = new Stack<Command>();
		this.editState = STATE_LINE;
		this.savedAddState = this.editState;
		this.grimpanPM = new XMLPropertyManager("/resources/grimpan.properties");
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
		if (editState.getStateType() == EditState.EDIT_MOVE){
			frameView.modeLbl.setText(String.format("Mode: %s  ", "이동 "));
		}
		else if(editState.getStateType() == EditState.EDIT_DELETE){
			frameView.modeLbl.setText(String.format("Mode: %s  ","삭제"));
		}
		else {
			this.savedAddState = editState;
			frameView.modeLbl.setText(String.format("Mode: %s  ", "추가 "));
			frameView.shapeLbl.setText(String.format("Shape: %s  ", Util.SHAPE_NAME[editState.getStateType()]));
		}
		
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
		frameView.setTitle("그림판 - "+saveFile.getPath());
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
	public int getSelectedShapeIndex() {
		return selectedShapeIndex;
	}

	/**
	 * @param selectedShapeIndex the selectedShape to set
	 */
	public void setSelectedShapeIndex(int selectedShapeIndex) {
		this.selectedShapeIndex = selectedShapeIndex;
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
		if (this.shapeFill) {
			if (shapeFillColor==null)
				this.shapeFillColor = Color.WHITE;
		}
		else {
			this.shapeFillColor = null;
		}
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
		if (shapeStrokeWidth==0f) {
			shapeStrokeColor = null;
		}
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
