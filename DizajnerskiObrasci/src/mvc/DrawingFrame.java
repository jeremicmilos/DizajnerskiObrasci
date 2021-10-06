package mvc;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.NotSerializableException;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class DrawingFrame extends JFrame {
	
	private DrawingView view = new DrawingView();
	private DrawingController controller;
	
	private ButtonGroup btnGroup = new ButtonGroup();
	private JToggleButton tglBtnPoint, tglBtnLine, tglBtnCircle, tglBtnDonut, tglBtnRectangle, tglBtnHexagon, tglBtnSelect;
	private JButton btnModify, btnDelete, btnEdgeColor, btnInnerColor, btnBringToFront, btnBringToBack, btnToFront, btnToBack, btnLoad;
	private JPanel pnlColors;
	private Color edgeColor = new Color(0, 0, 0);
	private Color innerColor = new Color (255, 255, 255);
	private JTextArea textArea;
	private JButton btnUndo;
	private JButton btnRedo;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	
	public DrawingFrame() {
		setBounds(100, 100, 800, 700);
		
		JPanel pnlMain = new JPanel();
		
		JPanel pnlActions = new JPanel();
		
		JPanel pnlShapes = new JPanel();
		
		pnlColors = new JPanel();
		
		JPanel pnlLog = new JPanel();
		
		JPanel pnlMovies = new JPanel();
		
		btnLoad = new JButton("LOAD - >");
		btnLoad.setEnabled(false);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.loadNext();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(pnlLog, GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
						.addComponent(view, GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(pnlActions, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(pnlShapes, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE))
								.addComponent(pnlMovies, GroupLayout.PREFERRED_SIZE, 585, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnLoad)
								.addComponent(pnlColors, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(pnlColors, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
						.addComponent(pnlShapes, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
						.addComponent(pnlActions, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(pnlMovies, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(23)
							.addComponent(btnLoad)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(view, GroupLayout.PREFERRED_SIZE, 356, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(pnlLog, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
		);
		
		btnBringToFront = new JButton("Bring to front");
		btnBringToFront.setEnabled(false);
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToFront();
			}
		});
		
		btnBringToBack = new JButton("Bring to back");
		btnBringToBack.setEnabled(false);
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToBack();
			}
		});
		
		btnToFront = new JButton("To front");
		btnToFront.setEnabled(false);
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.toFront();
			}
		});
		
		btnToBack = new JButton("To back");
		btnToBack.setEnabled(false);
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.toBack();
			}
		});
		GroupLayout gl_pnlMovies = new GroupLayout(pnlMovies);
		gl_pnlMovies.setHorizontalGroup(
			gl_pnlMovies.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlMovies.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnBringToFront, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnBringToBack, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnToFront, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnToBack, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(40, Short.MAX_VALUE))
		);
		gl_pnlMovies.setVerticalGroup(
			gl_pnlMovies.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_pnlMovies.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlMovies.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnToBack, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addComponent(btnToFront, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_pnlMovies.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnBringToFront, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
							.addComponent(btnBringToBack, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)))
					.addGap(27))
		);
		pnlMovies.setLayout(gl_pnlMovies);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_pnlLog = new GroupLayout(pnlLog);
		gl_pnlLog.setHorizontalGroup(
			gl_pnlLog.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
		);
		gl_pnlLog.setVerticalGroup(
			gl_pnlLog.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
		);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		pnlLog.setLayout(gl_pnlLog);
		
		btnEdgeColor = new JButton("Edge Color");
		btnEdgeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color tempColor = JColorChooser.showDialog(null, "Choose edge color", edgeColor);
				if(tempColor != null) {
					edgeColor = tempColor;
					btnEdgeColor.setBackground(edgeColor);
				}
			}
		});
		
		btnInnerColor = new JButton("Inner Color");
		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color tempColor  = JColorChooser.showDialog(null, "Choose inner color", innerColor);
				if(tempColor != null) {
					innerColor = tempColor;
					btnInnerColor.setBackground(innerColor);
				}
			}
		});
		GroupLayout gl_pnlColors = new GroupLayout(pnlColors);
		gl_pnlColors.setHorizontalGroup(
			gl_pnlColors.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlColors.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlColors.createParallelGroup(Alignment.LEADING)
						.addComponent(btnEdgeColor)
						.addComponent(btnInnerColor))
					.addContainerGap(32, Short.MAX_VALUE))
		);
		gl_pnlColors.setVerticalGroup(
			gl_pnlColors.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlColors.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnEdgeColor)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnInnerColor)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlColors.setLayout(gl_pnlColors);
		
		tglBtnPoint = new JToggleButton("Point");
		
		tglBtnLine = new JToggleButton("Line");
		
		tglBtnCircle = new JToggleButton("Circle");
		
		tglBtnDonut = new JToggleButton("Donut");
		
		tglBtnRectangle = new JToggleButton("Rectangle");
		
		tglBtnHexagon = new JToggleButton("Hexagon");
		
		
		GroupLayout gl_pnlShapes = new GroupLayout(pnlShapes);
		gl_pnlShapes.setHorizontalGroup(
			gl_pnlShapes.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlShapes.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlShapes.createParallelGroup(Alignment.LEADING, false)
						.addComponent(tglBtnLine, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
						.addComponent(tglBtnPoint, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 103, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlShapes.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(tglBtnDonut, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(tglBtnCircle, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlShapes.createParallelGroup(Alignment.LEADING, false)
						.addComponent(tglBtnRectangle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(tglBtnHexagon, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
					.addContainerGap(31, Short.MAX_VALUE))
		);
		gl_pnlShapes.setVerticalGroup(
			gl_pnlShapes.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlShapes.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlShapes.createParallelGroup(Alignment.BASELINE)
						.addComponent(tglBtnPoint)
						.addComponent(tglBtnCircle)
						.addComponent(tglBtnRectangle))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlShapes.createParallelGroup(Alignment.BASELINE)
						.addComponent(tglBtnLine)
						.addComponent(tglBtnDonut)
						.addComponent(tglBtnHexagon))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		pnlShapes.setLayout(gl_pnlShapes);
		
		tglBtnSelect = new JToggleButton("Select");
		tglBtnSelect.setEnabled(false);
		btnModify = new JButton("Modify");
		btnModify.setEnabled(false);
		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnUndo = new JButton("Undo");
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.undo();
			}
		});
		
		btnRedo = new JButton("Redo");
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		
		GroupLayout gl_pnlActions = new GroupLayout(pnlActions);
		gl_pnlActions.setHorizontalGroup(
			gl_pnlActions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlActions.createSequentialGroup()
					.addGroup(gl_pnlActions.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlActions.createSequentialGroup()
							.addContainerGap()
							.addComponent(tglBtnSelect)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnModify)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnDelete))
						.addGroup(gl_pnlActions.createSequentialGroup()
							.addGap(51)
							.addComponent(btnUndo, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnRedo, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		gl_pnlActions.setVerticalGroup(
			gl_pnlActions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlActions.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlActions.createParallelGroup(Alignment.BASELINE)
						.addComponent(tglBtnSelect)
						.addComponent(btnModify)
						.addComponent(btnDelete))
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addGroup(gl_pnlActions.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnRedo)
						.addComponent(btnUndo))
					.addContainerGap())
		);
		pnlActions.setLayout(gl_pnlActions);
		GroupLayout gl_pnlMain = new GroupLayout(pnlMain);
		gl_pnlMain.setHorizontalGroup(
			gl_pnlMain.createParallelGroup(Alignment.LEADING)
				.addGap(0, 766, Short.MAX_VALUE)
		);
		gl_pnlMain.setVerticalGroup(
			gl_pnlMain.createParallelGroup(Alignment.LEADING)
				.addGap(0, 368, Short.MAX_VALUE)
		);
		pnlMain.setLayout(gl_pnlMain);
		getContentPane().setLayout(groupLayout);
		
		btnGroup.add(tglBtnPoint);
		btnGroup.add(tglBtnLine);
		btnGroup.add(tglBtnCircle);
		btnGroup.add(tglBtnDonut);
		btnGroup.add(tglBtnRectangle);
		btnGroup.add(tglBtnHexagon);
		btnGroup.add(tglBtnSelect);
		
		
		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.drawShape(e, edgeColor, innerColor);
			}
		});

		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.modifyShape();
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.removeShape();
			}
		});

		pnlMain.add(view, GroupLayout.DEFAULT_SIZE);
		view.setBackground(Color.WHITE);
		view.setPreferredSize(new Dimension(1000, 800));
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		mntmNewMenuItem_3 = new JMenuItem("Open log");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.openLog();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_3);
		
		mntmNewMenuItem = new JMenuItem("Open painting");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.openPainting();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		mntmNewMenuItem_1 = new JMenuItem("Save log");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveLog();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		mntmNewMenuItem_2 = new JMenuItem("Save painting");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.savePainting();
				} catch (NotSerializableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);
	}
	
	public DrawingView getView() {
		return view;
	}

	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public JToggleButton getTglBtnPoint() {
		return tglBtnPoint;
	}

	public JToggleButton getTglBtnLine() {
		return tglBtnLine;
	}

	public JToggleButton getTglBtnCircle() {
		return tglBtnCircle;
	}

	public JToggleButton getTglBtnDonut() {
		return tglBtnDonut;
	}

	public JToggleButton getTglBtnRectangle() {
		return tglBtnRectangle;
	}

	public JToggleButton getTglBtnHexagon() {
		return tglBtnHexagon;
	}

	public JToggleButton getTglBtnSelect() {
		return tglBtnSelect;
	}

	public JButton getBtnModify() {
		return btnModify;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public JButton getBtnUndo() {
		return btnUndo;
	}

	public JButton getBtnRedo() {
		return btnRedo;
	}

	public JButton getBtnBringToFront() {
		return btnBringToFront;
	}

	public JButton getBtnBringToBack() {
		return btnBringToBack;
	}

	public JButton getBtnToFront() {
		return btnToFront;
	}

	public JButton getBtnToBack() {
		return btnToBack;
	}

	public JButton getBtnLoad() {
		return btnLoad;
	}
}
