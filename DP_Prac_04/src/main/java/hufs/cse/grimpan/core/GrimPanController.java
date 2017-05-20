/**
 * Created on 2015. 4. 3.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.core;

import hufs.cse.grimpan.command.AddCommand;
import hufs.cse.grimpan.command.Command;
import hufs.cse.grimpan.command.DelCommand;
import hufs.cse.grimpan.command.MoveCommand;

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

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author cskim
 *
 */
public class GrimPanController {
	
	ClassPathXmlApplicationContext cont;
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
					model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
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

	public void setDelShapeState(){                 // 삭제 버튼 누를 시, EditState를 STATE_DELETE로 바꿔줌으로써 삭제모드로 바뀜
		model.setEditState(model.STATE_DELETE);
		if (model.curDrawShape != null){
			model.shapeList
			.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
					model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
			model.curDrawShape = null;
		}
		drawPanelView.repaint();
	}
	
	public void setStrokeWithAction(String stroke) {                   // stroke = 팝업 창의 질문 내용 (한/영 변환)
		String strSW = model.grimpanPM.getPanProperties().getProperty("default.stroke.width");   // default "stroke.width" = 1
		String inputVal = JOptionPane.showInputDialog(stroke, strSW);
		if (inputVal!=null){
			model.setShapeStrokeWidth(Float.parseFloat(inputVal));
			strSW = inputVal;
		}
		else {
			model.setShapeStrokeWidth(Float.parseFloat(strSW));
		}
		model.grimpanPM.updateProperty("default.stroke.width", strSW);
		
	}

	/**
	 * 
	 */
	public void setBoundaryColorAction(String question) {               // question = 팝업 창의 title (한/영 변환)
		String strColor = model.grimpanPM.getPanProperties().getProperty("default.stroke.color");   // default "stroke color" = 회색
		Color defColor = new Color(Integer.parseInt(strColor, 16));
		Color color = 
				JColorChooser.showDialog(frameView, 
						question,                                // question (한/영 변환)
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
		model.grimpanPM.updateProperty("default.stroke.color", strColor);
	}
	
	public void setFillColorAction(String question) {             // question = 팝업 창의 title (한/영 변환)
		String strColor = model.grimpanPM.getPanProperties().getProperty("default.fill.color");     // default "Fill color" = 흰색
		Color defColor = new Color(Integer.parseInt(strColor, 16));

		Color color = 
				JColorChooser.showDialog(frameView, 
						question,                                  // question (한/영 변환)
						defColor);					
		if (color!=null){
			model.setShapeFillColor(color);
			defColor = color;
		}
		else {
			model.setShapeFillColor(Color.black);
		}
		strColor = String.format("%06X", (0xFFFFFF & defColor.getRGB()));
		System.out.println("Color="+strColor+"=");
		model.grimpanPM.updateProperty("default.fill.color", strColor);
	}

	/**
	 * 
	 */
	public void addShapeAction() {
		Command addCommand = new AddCommand(model, new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
				model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
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
		Command comm = model.undoCommandStack.pop();
		comm.undo();
		drawPanelView.repaint();
	}




}
