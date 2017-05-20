/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.core;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.border.EtchedBorder;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GrimPanFrameView extends JFrame {

	private static final long serialVersionUID = 1L;
	private GrimPanController controller = null;
	private GrimPanModel model = null;
	private GrimPanFrameView thisClass = this;
	private DrawPanelView drawPanelView = null;

	private JPanel contentPane;

	public JCheckBoxMenuItem jcmiFill = null;
	JFileChooser jFileChooser1 = null;
	JFileChooser jFileChooser2 = null;

	JMenuBar panMenuBar;	
	JMenu fileMenu;
	JMenuItem jmiNew;
	JMenuItem jmiOpen;
	JMenuItem jmiSave;
	JMenuItem jmiSaveAs;
	JMenuItem jmiSaveAsSvg;
	JMenuItem jmiExit;
	JMenu shapeMenu;
	JRadioButtonMenuItem rdbtnmntmLine;
	JRadioButtonMenuItem rdbtnmntmPen;	
	JRadioButtonMenuItem rdbtnmntmPoly;
	JRadioButtonMenuItem rdbtnmntmRegular;
	JRadioButtonMenuItem rdbtnmntmOval;
	JMenu editMenu;	
	JMenuItem jmiRemove;
	JMenuItem jmiMove;
	JMenu settingMenu;
	JMenuItem jmiStroke;
	JMenuItem jmiStorkeColor;	
	JMenuItem jmiFillColor;

	JMenu helpMenu;	
	JMenuItem jmiAbout;

	ButtonGroup btnGroup = new ButtonGroup();
	JPanel statusPanel;
	JLabel shapeLbl;
	JLabel sizeLbl;
	JMenuItem rmiAdd;
	JLabel messageLbl;

	public JLabel countLbl;                   // "count: " (한/영 변환)
	public static JLabel countNumLbl;         // count된 숫자 처리
	
	public JLabel modeLBL;
	private JMenuItem jmiRecovery;
	private JMenuItem jmiUndo;

	Properties prop;
	ClassPathXmlApplicationContext ctx;

	/**
	 * Create the frame.
	 */
	public GrimPanFrameView(GrimPanController controller, GrimPanModel model) {
		super();
		this.controller = controller;
		this.model = model;
		this.model.setFrameView(this);

		initialize();
	}

	public void isChecked(JMenuItem button, String check){   // default property에 따라 Check 표시 여부 처리 함수
		if(check.equals("yes")){                   // default.properties가 "yes"일 경우  check
			button.setSelected(true);
		}else                                      // default.properties가 "no"일 경우  check 해제
			button.setSelected(false);
	}
	
	public void defaultShape(){             // JRadioButtonMenuItem가 check된 Shape을 default Shape로 결정
		if(rdbtnmntmLine.isSelected())                     // Line이 체크되어 있을 경우
			model.setEditState(model.STATE_LINE);
		else if(rdbtnmntmPen.isSelected())                 // Pencil이 체크되어 있을 경우
			model.setEditState(model.STATE_PENCIL);
		else if(rdbtnmntmPoly.isSelected())                // Polygon이 체크되어 있을 경우
			model.setEditState(model.STATE_POLYGON);
		else if(rdbtnmntmRegular.isSelected())             // Regular가 체크되어 있을 경우
			model.setEditState(model.STATE_REGULAR);
		else                                               // Oval이 체크되어 있을 경우
			model.setEditState(model.STATE_OVAL);
	}
	
	private void initialize(){
		prop = model.grimpanPM.getPanProperties();
		ctx = model.context;
		System.out.println("title="+ctx.getMessage("title.label", null, model.locale));   // 메인 title (한/영 변환)
		setTitle(ctx.getMessage("title.label", null, model.locale));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);

		panMenuBar = new JMenuBar();
		setJMenuBar(panMenuBar);

		fileMenu = new JMenu(ctx.getMessage("title.label.file", null, model.locale));   // "파일" (한/영 변환)
		panMenuBar.add(fileMenu);

		jmiNew = new JMenuItem(ctx.getMessage("menu.label.new", null, model.locale));   // "새파일" (한/영 변환)
		jmiNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clearAllShape();
			}
		});
		fileMenu.add(jmiNew);

		jmiOpen = new JMenuItem(ctx.getMessage("menu.label.open", null, model.locale));  // "열기" (한/영 변환)
		jmiOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.openAction();
			}
		});
		fileMenu.add(jmiOpen);

		jmiSave = new JMenuItem(ctx.getMessage("menu.label.save", null, model.locale)); // "저장" (한/영 변환)
		jmiSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.saveAction();
			}
		});
		fileMenu.add(jmiSave);

		jmiSaveAs = new JMenuItem(ctx.getMessage("menu.label.save_as", null, model.locale));  // "다른 이름으로 저장" (한/영 변환)
		jmiSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.saveAsAction();
			}
		});
		fileMenu.add(jmiSaveAs);

		jmiExit = new JMenuItem(ctx.getMessage("menu.label.exit", null, model.locale));   // "닫기" (한/영 변환)
		jmiExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(jmiExit);
		
		jmiRecovery = new JMenuItem(ctx.getMessage("menu.label.recover", null, model.locale));   // "회복" (한/영 변환)
		jmiRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.recoveryAction();
			}
		});
		fileMenu.add(jmiRecovery);

		shapeMenu = new JMenu(ctx.getMessage("title.label.shape", null, model.locale));   // "모양" (한/영 변환)
		panMenuBar.add(shapeMenu);

		rdbtnmntmLine = new JRadioButtonMenuItem(ctx.getMessage("menu.label.line", null, model.locale));   // "직선" (한/영 변환)
		isChecked(rdbtnmntmLine, model.grimpanPM.getPanProperties().getProperty("default.checkLine"));   // default checkLine = "no"
		
		rdbtnmntmLine.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(model.STATE_LINE);
				}
			}
		});
		shapeMenu.add(rdbtnmntmLine);

		rdbtnmntmPen = new JRadioButtonMenuItem(ctx.getMessage("menu.label.pencil", null, model.locale));   // "연필" (한/영 변환)
		isChecked(rdbtnmntmPen, model.grimpanPM.getPanProperties().getProperty("default.checkPencil"));   // default checkPencil = "no"

		rdbtnmntmPen.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(model.STATE_PENCIL);
				}
			}
		});
		shapeMenu.add(rdbtnmntmPen);

		rdbtnmntmPoly = new JRadioButtonMenuItem(ctx.getMessage("menu.label.polygon", null, model.locale));	  // "다각형" (한/영 변환)
		isChecked(rdbtnmntmPoly, model.grimpanPM.getPanProperties().getProperty("default.checkPolygon"));   // default checkPolygon = "no"

		rdbtnmntmPoly.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(model.STATE_POLYGON);
				}
			}
		});
		shapeMenu.add(rdbtnmntmPoly);

		rdbtnmntmRegular = new JRadioButtonMenuItem(ctx.getMessage("menu.label.regular", null, model.locale));   // "정다각형" (한/영 변환)
		isChecked(rdbtnmntmRegular, model.grimpanPM.getPanProperties().getProperty("default.checkRegular"));    // default checkRegular = "yes"

		rdbtnmntmRegular.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(model.STATE_REGULAR);
					if (model.curDrawShape != null){
						model.shapeList
						.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
								model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
						model.curDrawShape = null;
					}
					Object[] possibleValues = { 
							"3", "4", "5", "6", "7",
							"8", "9", "10", "11", "12"
					};
					Object selectedValue = JOptionPane.showInputDialog(null,
							ctx.getMessage("menu.label.regular.question", null, model.locale),   // "정 n다각형 질문 창" (한/영 변환)
							ctx.getMessage("menu.label.regular.title", null, model.locale),     // "질문창 title" (한/영 변환)
							JOptionPane.INFORMATION_MESSAGE, null,
							possibleValues, possibleValues[0]);
					model.setNPolygon(Integer.parseInt((String)selectedValue));
					//drawPanelView.repaint();
				}
			}
		});
		shapeMenu.add(rdbtnmntmRegular);

		rdbtnmntmOval = new JRadioButtonMenuItem(ctx.getMessage("menu.label.oval", null, model.locale));   // "타원" (한/영 변환)
		isChecked(rdbtnmntmOval, model.grimpanPM.getPanProperties().getProperty("default.checkOval"));   // default checkOval = "no"
		
		rdbtnmntmOval.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(model.STATE_OVAL);
					if (model.curDrawShape != null){
						model.shapeList
						.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
								model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
						model.curDrawShape = null;
					}
				}
			}
		});
		shapeMenu.add(rdbtnmntmOval);

		btnGroup.add(rdbtnmntmLine);
		btnGroup.add(rdbtnmntmPen);
		btnGroup.add(rdbtnmntmPoly);
		btnGroup.add(rdbtnmntmRegular);
		btnGroup.add(rdbtnmntmOval);		

		editMenu = new JMenu(ctx.getMessage("title.label.edit", null, model.locale));   // "편집" (한/영 변환)
		panMenuBar.add(editMenu);

		jmiUndo = new JMenuItem(ctx.getMessage("menu.label.undo", null, model.locale));   // "되돌리기" (한/영 변환)
		jmiUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.undoAction();
			}
		});
		editMenu.add(jmiUndo);

		rmiAdd = new JMenuItem(ctx.getMessage("menu.label.add", null, model.locale));   // "추가" (한/영 변환)
		rmiAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setAddShapeState();
			}
		});
		editMenu.add(rmiAdd);

		jmiRemove = new JMenuItem(ctx.getMessage("menu.label.delete", null, model.locale));   // "삭제" (한/영 변환)
		jmiRemove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				controller.setDelShapeState();
			}
		});
		editMenu.add(jmiRemove);

		jmiMove = new JMenuItem(ctx.getMessage("menu.label.move", null, model.locale));   // "이동" (한/영 변환)
		jmiMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setMoveShapeState();
			}
		});
		editMenu.add(jmiMove);

		settingMenu = new JMenu(ctx.getMessage("title.label.setting", null, model.locale));   // "설정" (한/영 변환)
		panMenuBar.add(settingMenu);

		jmiStroke = new JMenuItem(ctx.getMessage("menu.label.stroke", null, model.locale));   // "선두께" (한/영 변환)
		jmiStroke.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setStrokeWithAction(ctx.getMessage("menu.label.stroke", null, model.locale));
			}
		});
		settingMenu.add(jmiStroke);

		jmiStorkeColor = new JMenuItem(ctx.getMessage("menu.label.stroke.color", null, model.locale));   // "테두리색" (한/영 변환)
		jmiStorkeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setBoundaryColorAction(ctx.getMessage("menu.label.stroke.color.question", null, model.locale));
			}
		});
		settingMenu.add(jmiStorkeColor);

		jcmiFill = new JCheckBoxMenuItem(ctx.getMessage("menu.label.fill", null, model.locale));   // "채움" (한/영 변환)
		isChecked(jcmiFill, model.grimpanPM.getPanProperties().getProperty("default.fill"));       // default fill = "yes"(채움으로 체크되있음)
		
		if(jcmiFill.isSelected())                                     // If jcmiFill is checked, Fill Shape
			model.setShapeFill(jcmiFill.getState());
		
		jcmiFill.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				model.setShapeFill(jcmiFill.getState());
			}
		});
		settingMenu.add(jcmiFill);

		jmiFillColor = new JMenuItem(ctx.getMessage("menu.label.fill.color", null, model.locale));   // "채움 색" (한/영 변환)
		jmiFillColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setFillColorAction(ctx.getMessage("menu.label.stroke.color.question", null, model.locale));
			}
		});
		settingMenu.add(jmiFillColor);

		helpMenu = new JMenu(ctx.getMessage("title.label.help", null, model.locale));   // "도움말" (한/영 변환)
		panMenuBar.add(helpMenu);

		jmiAbout = new JMenuItem(ctx.getMessage("menu.label.about", null, model.locale));   // "버전" (한/영 변환)
		jmiAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(thisClass,
						ctx.getMessage("menu.label.about.detail", null, model.locale),    // "버전 클릭 시, 세부사항" (한/영 변환)
						ctx.getMessage("menu.label.about", null, model.locale), JOptionPane.INFORMATION_MESSAGE);   // "버전 클릭 시, 세부사항 title" (한/영 변환)

			}
		});
		helpMenu.add(jmiAbout);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		drawPanelView = new DrawPanelView();
		drawPanelView.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				String sizeText = ctx.getMessage("menu.size", null, model.locale)+String.format(": %d x %d  ",    // 메인 하단 부분 "크기:" (한/영 변환)
						drawPanelView.getSize().width, drawPanelView.getSize().height);
				thisClass.sizeLbl.setText(sizeText);
			}
		});
		contentPane.add(drawPanelView, BorderLayout.CENTER);
		controller.setDrawPanelView(drawPanelView);

		statusPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) statusPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		statusPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(statusPanel, BorderLayout.SOUTH);

		sizeLbl = new JLabel("Size:               ");
		statusPanel.add(sizeLbl);

		shapeLbl = new JLabel("Shape:              ");
		shapeLbl.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(shapeLbl);

		modeLBL = new JLabel("Mode:               ");
		statusPanel.add(modeLBL);

		messageLbl = new JLabel("  ");
		statusPanel.add(messageLbl);

		countLbl = new JLabel(ctx.getMessage("menu.count", null, model.locale));   // 메인 하단 부분 "갯수:" (한/영 변환)
		statusPanel.add(countLbl);
		
		countNumLbl = new JLabel();        // 메인 하단 부분 "실제 갯수" (한/영 변환)
		statusPanel.add(countNumLbl);
		
		jFileChooser1 = new JFileChooser(model.getDefaultDir());
		jFileChooser1.setDialogTitle(ctx.getMessage("menu.label.open.title", null, model.locale));   // "열기" 클릭 시, 메뉴 title (한/영 변환)

		jFileChooser2 = new JFileChooser(model.getDefaultDir());
		jFileChooser2.setDialogType(JFileChooser.SAVE_DIALOG);
		jFileChooser2.setDialogTitle(ctx.getMessage("menu.label.save_as.title", null, model.locale));   // "다른 이름으로 저장" 클릭 시, 메뉴 title
		
		defaultShape();       // JRadioButtonMenuItem가 check된 Shape을 default Shape로 결정
	}
}
