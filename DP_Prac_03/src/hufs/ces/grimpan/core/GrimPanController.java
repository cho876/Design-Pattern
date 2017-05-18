package hufs.ces.grimpan.core;

import hufs.ces.grimpan.command.AddCommand;
import hufs.ces.grimpan.command.Command;
import hufs.ces.grimpan.command.DelCommand;
import hufs.ces.grimpan.command.MoveCommand;
import hufs.ces.grimpan.shape.EditState;
import hufs.ces.grimpan.shape.GrimShape;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class GrimPanController {
	
	private GrimPanModel model = GrimPanModel.getInstance();
	private GrimPanFrameView frameView = null;
	private DrawPanelView drawPanelView = null;

	public GrimPanController(){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createGrimPanFrameView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void createGrimPanFrameView(){
		frameView = new GrimPanFrameView(this, model);
		frameView.setVisible(true);
		model.setController(this);
		
	}
	public void openAction() {
		if (frameView.jFileChooser1.showOpenDialog(frameView) ==
				JFileChooser.APPROVE_OPTION) {
			File selFile = frameView.jFileChooser1.getSelectedFile();
			readShapeFromSaveFile(selFile);
			model.setSaveFile(selFile);
			drawPanelView.repaint();
		}
	}
	public void saveAction() {
		if (model.getSaveFile()==null){
			model.setSaveFile(new File(model.getDefaultDir()+"noname.grm"));
		}
		File selFile = model.getSaveFile();
		saveGrimPanData(selFile);	
	}
	public void saveAsAction() {
		if (frameView.jFileChooser2.showSaveDialog(frameView) ==
				JFileChooser.APPROVE_OPTION) {
			File selFile = frameView.jFileChooser2.getSelectedFile();
			model.setSaveFile(selFile);
			saveGrimPanData(selFile);
		}
	}
	public void readShapeFromSaveFile(File saveFile) {
		model.setSaveFile(saveFile);
		ObjectInputStream input;
		try {
			input =
				new ObjectInputStream(new FileInputStream(saveFile));
			model.shapeList = (ArrayList<GrimShape>) input.readObject();
			input.close();

		} catch (ClassNotFoundException e) {
			System.err.println("Class not Found");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("File not Found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO Exception");
			e.printStackTrace();
		}
	}
	public void saveGrimPanData(File saveFile){
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(new FileOutputStream(saveFile));
			output.writeObject(model.shapeList);
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not Found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO Exception");
			e.printStackTrace();
		}
	}

	/**
	 * @return the drawPanelView
	 */
	public DrawPanelView getDrawPanelView() {
		return drawPanelView;
	}

	/**
	 * @param drawPanelView the drawPanelView to set
	 */
	public void setDrawPanelView(DrawPanelView drawPanelView) {
		this.drawPanelView = drawPanelView;
	}

	/**
	 * 
	 */
	public void clearAllShape() {
		model.shapeList.clear();
		model.curDrawShape = null;
		model.polygonPoints.clear();
		drawPanelView.repaint();
	}

	/**
	 * 
	 */
	public void setMoveShapeState() {
		model.setEditState(model.STATE_MOVE);
		if (model.curDrawShape != null){
			model.shapeList
			.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
					model.getShapeStrokeColor(), model.getShapeFillColor()));
			model.curDrawShape = null;
		}
		drawPanelView.repaint();
	}
	/**
	 * 
	 */
	public void setAddShapeState() {
		model.setEditState(model.savedAddState);
		
	}

	public void setDelShapeState(){   // 삭제 버튼 누를 시, EditState를 STATE_DELETE로 바꿔줌으로써 삭제모드로 바뀜
		model.setEditState(model.STATE_DELETE);
		if (model.curDrawShape != null){
			model.shapeList
			.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
					model.getShapeStrokeColor(), model.getShapeFillColor()));
			model.curDrawShape = null;
		}
		drawPanelView.repaint();
	}
	/**
	 * 
	 */
	public void setStrokeWithAction() {
		String strSW = model.grimpanPM.getPanProperties().getProperty("default.stroke.width");
		String inputVal = JOptionPane.showInputDialog("선두께", strSW);
		if (inputVal!=null){
			model.setShapeStrokeWidth(Float.parseFloat(inputVal));
			strSW = inputVal;
		}
		else {
			model.setShapeStrokeWidth(Float.parseFloat(strSW));
		}
		model.grimpanPM.updatePropertyOfXML("default.stroke.width", strSW);
		
	}

	/**
	 * 
	 */
	public void setBoundaryColorAction() {
		String strColor = model.grimpanPM.getPanProperties().getProperty("default.stroke.color");
		Color defColor = new Color(Integer.parseInt(strColor, 16));

		Color color = 
				JColorChooser.showDialog(frameView, 
						"Choose a color",
						defColor);					
		if (color!=null){
			model.setShapeStrokeColor(color);
			defColor = color;
		}
		else {
			model.setShapeStrokeColor(defColor);
		}
		strColor = String.format("%06X", (0xFFFFFF & defColor.getRGB()));
		System.out.println("Color="+strColor+"=");
		model.grimpanPM.updatePropertyOfXML("default.stroke.color", strColor);
	}

	/**
	 * 
	 */
	public void setFillColorAction() {
		Color color = 
				JColorChooser.showDialog(frameView, 
						"Choose a color",
						Color.black);					
		if (color!=null){
			model.setShapeFillColor(color);
		}
		else {
			//model.setShapeFillColor(Color.black);
		}
	}

	/**
	 * 
	 */
	public void addShapeAction() {
		Command addCommand = new AddCommand(model, new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
				model.getShapeStrokeColor(), model.getShapeFillColor()));
		model.undoCommandStack.push(addCommand);// save for undo
		addCommand.execute();

	}

	/**
	 * 
	 */
	public void moveShapeAction() {
		Command moveCommand = new MoveCommand(model, model.getSavedPositionShape());
		model.undoCommandStack.push(moveCommand);// save for undo
		moveCommand.execute();
	}
	/**
	 * 
	 */
	public void deleteShapeAction(int index){  
		Command DelCommand = new DelCommand(model, model.shapeList.get(index));  // index에 해당하는 도형 DelCommand로 전달
		model.undoCommandStack.push(DelCommand);// save for undo
		DelCommand.execute();
	}
	
	public void recoveryAction() {
		// TODO Auto-generated method stub
		
	}

	public void undoAction() {
		if (model.undoCommandStack.isEmpty())
			return;
		
		Command comm = model.undoCommandStack.pop();
		comm.undo();
		drawPanelView.repaint();
	}
}
