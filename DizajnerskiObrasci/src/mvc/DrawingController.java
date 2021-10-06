package mvc;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import adapter.HexagonAdapter;
import command.CmdAddShape;
import command.CmdBringToBack;
import command.CmdBringToFront;
import command.CmdDeselectShape;
import command.CmdModifyCircle;
import command.CmdModifyDonut;
import command.CmdModifyHexagon;
import command.CmdModifyLine;
import command.CmdModifyPoint;
import command.CmdModifyRectangle;
import command.CmdRemoveShape;
import command.CmdSelectShape;
import command.CmdToBack;
import command.CmdToFront;
import command.Command;
import drawDialogues.DrawCircle;
import drawDialogues.DrawDonut;
import drawDialogues.DrawHexagon;
import drawDialogues.DrawRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import modification.DlgCircleModify;
import modification.DlgDonutModify;
import modification.DlgHexagonModify;
import modification.DlgLineModify;
import modification.DlgPointModify;
import modification.DlgRectangleModify;
import observer.BtnObserver;
import observer.BtnObserverUpdate;
import strategy.SaveLog;
import strategy.SaveManager;
import strategy.SavePainting;

public class DrawingController {
	private DrawingModel model;
	private DrawingFrame frame;

	DlgPointModify dlgPointModify = new DlgPointModify();
	DlgLineModify dlgLineModify = new DlgLineModify();
	DlgDonutModify dlgDonutModify = new DlgDonutModify();
	DlgRectangleModify dlgRectangleModify = new DlgRectangleModify();
	DlgCircleModify dlgCircleModify = new DlgCircleModify();
	DlgHexagonModify dlgHexagonModify = new DlgHexagonModify();

	private Point startPoint;

	private ArrayList<Shape> selectedShapes = new ArrayList<Shape>();

	private ArrayList<Shape> undoShapesList = new ArrayList<Shape>();
	private ArrayList<Shape> redoShapesList = new ArrayList<Shape>();

	private Command command;

	private int undoCounter = 0;
	private int redoCounter = 0;

	private BtnObserver btnObserver = new BtnObserver();
	private BtnObserverUpdate btnObserverUpdate;

	private Stack<Command> undoStack = new Stack<Command>();
	private Stack<Command> redoStack = new Stack<Command>();
	
	private ArrayList<String> listLog = new ArrayList<String>();
	private int counterLog = 0;

	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;

		btnObserverUpdate = new BtnObserverUpdate(frame);
		btnObserver.addPropertyChangeListener(btnObserverUpdate);
	}

	// -------------------------DRAW------------------------

	public void drawShape(MouseEvent e, Color edgeColor, Color innerColor) {
		Point center = new Point(e.getX(), e.getY());
		Shape selected;
		Shape shape;

		if (frame.getTglBtnSelect().isSelected()) {
			selected = null; // svi selektovani
			shape = null; // prolazi kroz sve shapes i uzima vrednost onog koji je trenutno kliknuto

			Iterator<Shape> it = model.getShapes().iterator();
			while (it.hasNext()) {
				shape = it.next();
				if (shape.contains(e.getX(), e.getY())) {
					selected = shape;
				}
			}

			if (selected != null) {
				if (selected.isSelected()) {
					command = new CmdDeselectShape(this, selected);
					command.execute();
					undoStack.push(command);
					frame.getTextArea().append(command.toString());
				} else {
					command = new CmdSelectShape(this, selected);
					command.execute();
					undoStack.push(command);
					frame.getTextArea().append(command.toString());
				}
				redoStack.clear();
				undoCounter++;
			}

			checkUndoRedo();
			visibleButtons();
			frame.repaint();
		} else {
			if (frame.getTglBtnPoint().isSelected()) {
				Point p = new Point(e.getX(), e.getY());
				p.setColor(edgeColor);
				command = new CmdAddShape(model, p);
				command.execute();
				frame.getTextArea().append(command.toString());
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
			} else if (frame.getTglBtnLine().isSelected()) {
				if (startPoint == null) {
					startPoint = new Point(e.getX(), e.getY());
					return;
				}
				Point endPoint = new Point(e.getX(), e.getY());
				Line l = new Line(startPoint, endPoint);
				l.setColor(edgeColor);
				command = new CmdAddShape(model, l);
				command.execute();
				frame.getTextArea().append(command.toString());
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
				startPoint = null;
			} else if (frame.getTglBtnCircle().isSelected()) {
				DrawCircle drawCircle = new DrawCircle();
				drawCircle.setVisible(true);
				if (drawCircle.isConfirmed()) {
					Circle c = new Circle(center, Integer.parseInt(drawCircle.getTxtRadius().getText()));
					c.setColor(edgeColor);
					c.setInnerColor(innerColor);
					command = new CmdAddShape(model, c);
					command.execute();
					frame.getTextArea().append(command.toString());
					undoCounter++;
					undoStack.push(command);
					redoStack.clear();
				}
			} else if (frame.getTglBtnDonut().isSelected()) {
				DrawDonut drawDonut = new DrawDonut();
				drawDonut.setVisible(true);
				if (drawDonut.isConfirmed()) {
					Donut d = new Donut(center, Integer.parseInt(drawDonut.getTxtRadius().getText()),
							Integer.parseInt(drawDonut.getTxtInnerRadius().getText()));
					d.setColor(edgeColor);
					d.setInnerColor(innerColor);
					command = new CmdAddShape(model, d);
					command.execute();
					frame.getTextArea().append(command.toString());
					undoCounter++;
					undoStack.push(command);
					redoStack.clear();
				}
			} else if (frame.getTglBtnRectangle().isSelected()) {
				DrawRectangle drawRectangle = new DrawRectangle();
				drawRectangle.setVisible(true);
				if (drawRectangle.isConfirmed()) {
					Rectangle r = new Rectangle(center, Integer.parseInt(drawRectangle.getTxtHeight().getText()),
							Integer.parseInt(drawRectangle.getTxtWidth().getText()));
					r.setColor(edgeColor);
					r.setInnerColor(innerColor);
					command = new CmdAddShape(model, r);
					command.execute();
					frame.getTextArea().append(command.toString());
					undoCounter++;
					undoStack.push(command);
					redoStack.clear();
				}
			} else if (frame.getTglBtnHexagon().isSelected()) {
				DrawHexagon drawHexagon = new DrawHexagon();
				drawHexagon.setVisible(true);
				if (drawHexagon.isConfirmed()) {
					HexagonAdapter h = new HexagonAdapter(center,
							Integer.parseInt(drawHexagon.getTxtRadius().getText()));
					h.setHexagonBorderColor(edgeColor);
					h.setHexagonInnerColor(innerColor);
					command = new CmdAddShape(model, h);
					command.execute();
					frame.getTextArea().append(command.toString());
					undoCounter++;
					undoStack.push(command);
					redoStack.clear();
				}
			}
		}
		checkUndoRedo();
		visibleButtons();
		frame.getView().repaint();
	}

	// -------------------------REMOVE------------------------

	public void removeShape() {
		Shape shape;

		for (int i = selectedShapes.size() - 1; i >= 0; i--) {
			shape = selectedShapes.get(0);
			command = new CmdRemoveShape(model, shape, model.getShapes().indexOf(shape));
			command.execute();

			frame.getTextArea().append(command.toString());
			undoShapesList.add(shape);
			undoStack.push(command);
			undoCounter++;
			selectedShapes.remove(shape);
		}
		redoStack.clear();
		checkUndoRedo();
		visibleButtons();
		frame.getView().repaint();
	}

	// -------------------------MODIFY------------------------

	public void modifyShape() {
		if (selectedShapes.get(0) instanceof Point) {
			Point oldPoint = (Point) selectedShapes.get(0);

			dlgPointModify.getTxtXcoordinate().setText(Integer.toString(oldPoint.getX()));
			dlgPointModify.getTxtYcoordinate().setText(Integer.toString(oldPoint.getY()));
			dlgPointModify.getBtnColor().setBackground(oldPoint.getColor());
			dlgPointModify.setVisible(true);

			if (dlgPointModify.isConfirmed()) {
				Point newPoint = new Point(Integer.parseInt(dlgPointModify.getTxtXcoordinate().getText()),
						Integer.parseInt(dlgPointModify.getTxtYcoordinate().getText()), true,
						dlgPointModify.getBtnColor().getBackground());
				command = new CmdModifyPoint(oldPoint, newPoint);
				command.execute();
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
				frame.getTextArea().append(command.toString());
			}
		} else if (selectedShapes.get(0) instanceof Line) {
			Line oldLine = (Line) selectedShapes.get(0);

			dlgLineModify.getTxtStartXcoordinate().setText(Integer.toString(oldLine.getStartPoint().getX()));
			dlgLineModify.getTxtStartYcoordinate().setText(Integer.toString(oldLine.getStartPoint().getY()));
			dlgLineModify.getTxtEndXcoordinate().setText(Integer.toString(oldLine.getEndPoint().getX()));
			dlgLineModify.getTxtEndYcoordinate().setText(Integer.toString(oldLine.getEndPoint().getY()));
			dlgLineModify.getBtnColor().setBackground(oldLine.getColor());
			dlgLineModify.setVisible(true);

			if (dlgLineModify.isConfirmed()) {
				Line newLine = new Line(
						new Point(Integer.parseInt(dlgLineModify.getTxtStartXcoordinate().getText()),
								Integer.parseInt(dlgLineModify.getTxtStartYcoordinate().getText())),
						new Point(Integer.parseInt(dlgLineModify.getTxtEndXcoordinate().getText()),
								Integer.parseInt(dlgLineModify.getTxtEndYcoordinate().getText())),
						true, dlgLineModify.getBtnColor().getBackground());
				command = new CmdModifyLine(oldLine, newLine);
				command.execute();
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
				frame.getTextArea().append(command.toString());
			}

		} else if (selectedShapes.get(0) instanceof Donut) {
			Donut oldDonut = (Donut) selectedShapes.get(0);

			dlgDonutModify.getTxtXcoordinate().setText(Integer.toString(oldDonut.getCenter().getX()));
			dlgDonutModify.getTxtYcoordinate().setText(Integer.toString(oldDonut.getCenter().getY()));
			dlgDonutModify.getTxtRadius().setText(Integer.toString(oldDonut.getRadius()));
			dlgDonutModify.getTxtInnerRadius().setText(Integer.toString(oldDonut.getInnerRadius()));
			dlgDonutModify.getBtnEdgeColor().setBackground(oldDonut.getColor());
			dlgDonutModify.getBtnInnerColor().setBackground(oldDonut.getInnerColor());

			dlgDonutModify.setVisible(true);

			if (dlgDonutModify.isConfirmed()) {
				Donut newDonut = new Donut(
						new Point(Integer.parseInt(dlgDonutModify.getTxtXcoordinate().getText()),
								Integer.parseInt(dlgDonutModify.getTxtYcoordinate().getText())),
						Integer.parseInt(dlgDonutModify.getTxtRadius().getText()),
						Integer.parseInt(dlgDonutModify.getTxtInnerRadius().getText()), true,
						dlgDonutModify.getBtnEdgeColor().getBackground(),
						dlgDonutModify.getBtnInnerColor().getBackground());
				command = new CmdModifyDonut(oldDonut, newDonut);
				command.execute();	
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
				frame.getTextArea().append(command.toString());
			}
		} else if (selectedShapes.get(0) instanceof Rectangle) {
			Rectangle oldRectangle = (Rectangle) selectedShapes.get(0);

			dlgRectangleModify.getTxtXcoordinate().setText(Integer.toString(oldRectangle.getUpperLeftPoint().getX()));
			dlgRectangleModify.getTxtYcoordinate().setText(Integer.toString(oldRectangle.getUpperLeftPoint().getY()));
			dlgRectangleModify.getTxtHeight().setText(Integer.toString(oldRectangle.getHeight()));
			dlgRectangleModify.getTxtWidth().setText(Integer.toString(oldRectangle.getWidth()));
			dlgRectangleModify.getBtnEdgeColor().setBackground(oldRectangle.getColor());
			dlgRectangleModify.getBtnInnerColor().setBackground(oldRectangle.getInnerColor());

			dlgRectangleModify.setVisible(true);

			if (dlgRectangleModify.isConfirm()) {
				Rectangle newRectangle = new Rectangle(
						new Point(Integer.parseInt(dlgRectangleModify.getTxtXcoordinate().getText()),
								Integer.parseInt(dlgRectangleModify.getTxtYcoordinate().getText())),
						Integer.parseInt(dlgRectangleModify.getTxtHeight().getText()),
						Integer.parseInt(dlgRectangleModify.getTxtWidth().getText()), true,
						dlgRectangleModify.getBtnEdgeColor().getBackground(),
						dlgRectangleModify.getBtnInnerColor().getBackground());

				command = new CmdModifyRectangle(oldRectangle, newRectangle);
				command.execute();
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
				frame.getTextArea().append(command.toString());
			}
		} else if (selectedShapes.get(0) instanceof Circle) {
			Circle oldCircle = (Circle) selectedShapes.get(0);

			dlgCircleModify.getTxtXcoordinate().setText(Integer.toString(oldCircle.getCenter().getX()));
			dlgCircleModify.getTxtYcoordinate().setText(Integer.toString(oldCircle.getCenter().getY()));
			dlgCircleModify.getTxtRadius().setText(Integer.toString(oldCircle.getRadius()));
			dlgCircleModify.getBtnEdgeColor().setBackground(oldCircle.getColor());
			dlgCircleModify.getBtnInnerColor().setBackground(oldCircle.getInnerColor());

			dlgCircleModify.setVisible(true);

			if (dlgCircleModify.isConfirmed()) {
				Circle newCircle = new Circle(
						new Point(Integer.parseInt(dlgCircleModify.getTxtXcoordinate().getText()),
								Integer.parseInt(dlgCircleModify.getTxtYcoordinate().getText())),
						Integer.parseInt(dlgCircleModify.getTxtRadius().getText()), true,
						dlgCircleModify.getBtnEdgeColor().getBackground(),
						dlgCircleModify.getBtnInnerColor().getBackground());
				command = new CmdModifyCircle(oldCircle, newCircle);
				command.execute();
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
				frame.getTextArea().append(command.toString());
			}
		} else if (selectedShapes.get(0) instanceof HexagonAdapter) {
			HexagonAdapter oldHexagon = (HexagonAdapter) selectedShapes.get(0);
			dlgHexagonModify.getTxtXCoordinate().setText(Integer.toString(oldHexagon.getHexagon().getX()));
			dlgHexagonModify.getTxtYCoordinate().setText(Integer.toString(oldHexagon.getHexagon().getY()));
			dlgHexagonModify.getTxtRadius().setText(Integer.toString(oldHexagon.getHexagonRadius()));
			dlgHexagonModify.getBtnEdgeColor().setBackground(oldHexagon.getHexagonBorderColor());
			dlgHexagonModify.getBtnInnerColor().setBackground(oldHexagon.getHexagonInnerColor());

			dlgHexagonModify.setVisible(true);

			if (dlgHexagonModify.isConfirmed()) {
				HexagonAdapter newHexagon = new HexagonAdapter(
						new Point(Integer.parseInt(dlgHexagonModify.getTxtXCoordinate().getText()),
								Integer.parseInt(dlgHexagonModify.getTxtYCoordinate().getText())),
						Integer.parseInt(dlgHexagonModify.getTxtRadius().getText()), true,
						dlgHexagonModify.getBtnEdgeColor().getBackground(),
						dlgHexagonModify.getBtnInnerColor().getBackground());

				command = new CmdModifyHexagon(oldHexagon, newHexagon);
				command.execute();
				undoCounter++;
				undoStack.push(command);
				redoStack.clear();
				frame.getTextArea().append(command.toString());
			}
		}
		checkUndoRedo();
		visibleButtons();
		frame.getView().repaint();
	}

	// -------------------------CHECK UNDO REDO------------------------

	public void checkUndoRedo() {
		if (undoCounter < 1) {
			frame.getBtnUndo().setEnabled(false);
		} else {
			frame.getBtnUndo().setEnabled(true);
		}

		if (redoCounter < 1 || redoStack.isEmpty()) {
			frame.getBtnRedo().setEnabled(false);
		} else {
			frame.getBtnRedo().setEnabled(true);
		}
	}

	public ArrayList<Shape> getSelectedShapes() {
		return selectedShapes;
	}

	// -------------------------UNDO------------------------
	/*
	 * public void undo() { command = undoStack.peek(); command.unexecute(); if
	 * (command instanceof CmdRemoveShape) {
	 * redoShapesList.add(undoShapesList.get(undoShapesList.size() - 1));
	 * selectedShapes.add(undoShapesList.get(undoShapesList.size() - 1));
	 * undoShapesList.remove(undoShapesList.size() - 1); }
	 * frame.getTextArea().append("Undo " + undoStack.peek().toString());
	 * undoCounter--; redoCounter++; frame.getView().repaint(); undoStack.pop();
	 * redoStack.push(command); checkUndoRedo();
	 * 
	 * }
	 */

	// -----------------UNDO ZA VRACANJE NEKOLIKO OBLIKA
	// ODJEDNOM------------------------

	public void undo() {
		command = undoStack.peek();

		if (command instanceof CmdRemoveShape) {
			while (command instanceof CmdRemoveShape) {
				command.unexecute();
				this.redoShapesList.add(this.undoShapesList.get(this.undoShapesList.size() - 1));
				this.selectedShapes.add(this.undoShapesList.get(this.undoShapesList.size() - 1));
				this.undoShapesList.remove(this.undoShapesList.size() - 1);
				this.frame.getTextArea().append("Undo " + undoStack.peek().toString());
				undoCounter--;
				redoCounter++;
				undoStack.pop();
				redoStack.push(command);
				command = undoStack.peek();
			}
		} else {
			command.unexecute();
			this.frame.getTextArea().append("Undo " + undoStack.peek().toString());
			undoCounter--;
			redoCounter++;
			undoStack.pop();
			redoStack.push(command);
		}
		frame.repaint();
		checkUndoRedo();
		visibleButtons();
	}

	// -------------------------REDO------------------------
	/*
	 * public void redo() { command = redoStack.peek(); command.execute(); if
	 * (command instanceof CmdRemoveShape) {
	 * undoShapesList.add(redoShapesList.get(redoShapesList.size() - 1));
	 * selectedShapes.remove(redoShapesList.get(redoShapesList.size() - 1));
	 * redoShapesList.remove(redoShapesList.size() - 1); }
	 * frame.getTextArea().append("Redo " + redoStack.peek().toString());
	 * undoCounter++; redoCounter--; frame.getView().repaint(); redoStack.pop();
	 * undoStack.push(command); checkUndoRedo();
	 * 
	 * }
	 */

	// ----------------REDO ZA VRACANJE VISE OBLIKA ODJEDNOM---------------------

	public void redo() {
		command = redoStack.peek();

		if (command instanceof CmdRemoveShape) {
			while (command instanceof CmdRemoveShape) {
				command.execute();
				this.undoShapesList.add(this.redoShapesList.get(this.redoShapesList.size() - 1));
				this.selectedShapes.remove(this.redoShapesList.get(this.redoShapesList.size() - 1));
				this.redoShapesList.remove(this.redoShapesList.size() - 1);
				this.frame.getTextArea().append("Redo " + redoStack.peek().toString());
				undoCounter++;
				redoCounter--;
				redoStack.pop();
				undoStack.push(command);
				if (!redoStack.isEmpty()) {
					command = redoStack.peek();
				} else {
					command = null;
				}

			}
		} else {
			command.execute();
			this.frame.getTextArea().append("Redo " + redoStack.peek().toString());
			undoCounter++;
			redoCounter--;
			redoStack.pop();
			undoStack.push(command);
		}

		checkUndoRedo();
		visibleButtons();
		frame.repaint();
	}

	// -----------------BRING TO FRONT------------------

	public void bringToFront() {
		Shape shape = selectedShapes.get(0);
		CmdBringToFront cmdBringToFront = new CmdBringToFront(model, shape);
		cmdBringToFront.execute();
		frame.getTextArea().append(cmdBringToFront.toString());
		undoStack.push(cmdBringToFront);
		undoCounter++;
		redoStack.clear();
		checkUndoRedo();
		visibleButtons();
		frame.getView().repaint();

	}

	// -----------------BRING TO BACK------------------

	public void bringToBack() {
		Shape shape = selectedShapes.get(0);
		CmdBringToBack cmdBringToBack = new CmdBringToBack(model, shape);
		cmdBringToBack.execute();
		frame.getTextArea().append(cmdBringToBack.toString());
		undoStack.push(cmdBringToBack);
		undoCounter++;
		redoStack.clear();
		checkUndoRedo();
		visibleButtons();
		frame.getView().repaint();
	}

	// -----------------TO FRONT------------------

	public void toFront() {
		Shape shape = selectedShapes.get(0);
		CmdToFront cmdToFront = new CmdToFront(model, shape);
		cmdToFront.execute();
		frame.getTextArea().append(cmdToFront.toString());
		undoStack.push(cmdToFront);
		undoCounter++;
		redoStack.clear();
		checkUndoRedo();
		visibleButtons();
		frame.getView().repaint();

	}

	// -----------------TO BACK------------------

	public void toBack() {
		Shape shape = selectedShapes.get(0);
		CmdToBack cmdToBack = new CmdToBack(model, shape);
		cmdToBack.execute();
		frame.getTextArea().append(cmdToBack.toString());
		undoStack.push(cmdToBack);
		undoCounter++;
		redoStack.clear();
		checkUndoRedo();
		visibleButtons();
		frame.getView().repaint();
	}

	// -----------------OBSERVER---------------

	public void visibleButtons() {
		if (model.getShapes().size() != 0) {
			btnObserver.setSelectBtnEnabled(true);
			if (selectedShapes.size() != 0) {
				if (selectedShapes.size() == 1) {
					btnObserver.setModifyBtnEnabled(true);
					btnUpdate();
				} else {
					btnObserver.setModifyBtnEnabled(false);

					btnObserver.setBringToFrontBtnEnabled(false);
					btnObserver.setBringToBackBtnEnabled(false);
					btnObserver.setToFrontBtnEnabled(false);
					btnObserver.setToBackBtnEnabled(false);
				}
				btnObserver.setDeleteBtnEnabled(true);
			} else {
				btnObserver.setDeleteBtnEnabled(false);
				btnObserver.setModifyBtnEnabled(false);

				btnObserver.setBringToFrontBtnEnabled(false);
				btnObserver.setBringToBackBtnEnabled(false);
				btnObserver.setToFrontBtnEnabled(false);
				btnObserver.setToBackBtnEnabled(false);
			}
		} else {
			btnObserver.setDeleteBtnEnabled(false);
			btnObserver.setModifyBtnEnabled(false);
			btnObserver.setSelectBtnEnabled(false);

			btnObserver.setBringToFrontBtnEnabled(false);
			btnObserver.setBringToBackBtnEnabled(false);
			btnObserver.setToFrontBtnEnabled(false);
			btnObserver.setToBackBtnEnabled(false);
		}
	}

	// -----OBSERVER------------------BRING TO FRONT, BRING TO BACK, TO FRONT, TO
	// BACK BUTTONS ------------------------

	private void btnUpdate() {
		Iterator<Shape> iterator = this.model.getShapes().iterator();
		Shape shape;

		while (iterator.hasNext()) {
			shape = iterator.next();

			if (shape.isSelected()) {
				if (this.model.getShapes().size() == 1) {
					btnObserver.setBringToFrontBtnEnabled(false);
					btnObserver.setBringToBackBtnEnabled(false);
					btnObserver.setToFrontBtnEnabled(false);
					btnObserver.setToBackBtnEnabled(false);
				} else {
					if (shape.equals(model.getOneShape(this.model.getShapes().size() - 1))) { // proverava da li je prvi
																								// selektovan
						btnObserver.setBringToFrontBtnEnabled(false);
						btnObserver.setBringToBackBtnEnabled(true);
						btnObserver.setToFrontBtnEnabled(false);
						btnObserver.setToBackBtnEnabled(true);
					} else if (shape.equals(model.getOneShape(0))) { // kontra od prethodnog
						btnObserver.setBringToFrontBtnEnabled(true);
						btnObserver.setBringToBackBtnEnabled(false);
						btnObserver.setToFrontBtnEnabled(true);
						btnObserver.setToBackBtnEnabled(false);
					} else {
						btnObserver.setBringToFrontBtnEnabled(true); // provera da li je selektovan neki a da nije na
																		// jednom i drugom kraju
						btnObserver.setBringToBackBtnEnabled(true);
						btnObserver.setToFrontBtnEnabled(true);
						btnObserver.setToBackBtnEnabled(true);
					}
				}
			}
		}
	}

	public void savePainting() throws IOException, NotSerializableException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save painting");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin", "bin");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);

		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToSave = fileChooser.getSelectedFile();
			File logToSave;
			String filePath = paintingToSave.getAbsolutePath();
			if (!filePath.endsWith(".bin") && !filePath.contains(".")) {
				paintingToSave = new File(filePath + ".bin");
				logToSave = new File(filePath + ".txt");
			}

			String filename = paintingToSave.getPath();
			if (filename.substring(filename.lastIndexOf("."), filename.length()).contains(".bin")) {
				filename = paintingToSave.getAbsolutePath().substring(0, filename.lastIndexOf(".")) + ".txt";
				logToSave = new File(filename);
				SaveManager savePainting = new SaveManager(new SavePainting());
				SaveManager saveLog = new SaveManager(new SaveLog());
				savePainting.save(model, paintingToSave);
				saveLog.save(frame, logToSave);
			} else {
				JOptionPane.showMessageDialog(null, "Wrong file extension!");
			}
		}
	}

	public void openPainting() throws IOException, ClassNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(".bin", "bin");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(fileNameExtensionFilter);

		fileChooser.setDialogTitle("Open painting");
		int userSelection = fileChooser.showOpenDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToLoad = fileChooser.getSelectedFile();
			loadPainting(paintingToLoad);

		} 
	}

	@SuppressWarnings("unchecked")
	public void loadPainting(File paintingToLoad) throws FileNotFoundException, IOException, ClassNotFoundException {
		frame.getTextArea().setText("");

		File file = new File(paintingToLoad.getAbsolutePath().replace("bin", "txt"));

		if (file.length() == 0) {
			System.out.println("\"" + paintingToLoad.getName() + "\" file is empty!");
			return;
		}

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String logLine;

		while ((logLine = bufferedReader.readLine()) != null) {
			frame.getTextArea().append(logLine + "\n");
		}
		bufferedReader.close();

		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(paintingToLoad));
		try {

			model.getShapes().addAll((ArrayList<Shape>) objectInputStream.readObject());
			objectInputStream.close();

		} catch (InvalidClassException ice) {
			ice.printStackTrace();
		} catch (SocketTimeoutException ste) {
			ste.printStackTrace();
		} catch (EOFException eofe) {
			eofe.printStackTrace();
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		frame.getView().repaint();
	}

	public void saveLog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save log");
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(".txt", "txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(fileNameExtensionFilter);

		if (fileChooser.showSaveDialog(frame.getParent()) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String filePath = file.getAbsolutePath();
			File logToSave = new File(filePath + ".txt");

			SaveManager manager = new SaveManager(new SaveLog());
			manager.save(frame, logToSave);
		}
		frame.getView().repaint();
	}

	public void openLog() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open log");
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(".txt", "txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(fileNameExtensionFilter);

		int userSelection = fileChooser.showOpenDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File logToLoad = fileChooser.getSelectedFile();
			loadLog(logToLoad);
		}
	}

	public void loadLog(File logToLoad) throws IOException {
		try {
			frame.getTextArea().setText("");
			if (logToLoad.length() == 0) {
				System.out.println("\"" + logToLoad.getName() + "\" file is empty!");
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(logToLoad));
			String stringLine;

			while ((stringLine = br.readLine()) != null) {
				 listLog.add(stringLine);
			}
			br.close();
				frame.getTglBtnPoint().setEnabled(false);
				frame.getTglBtnLine().setEnabled(false);
				frame.getTglBtnCircle().setEnabled(false);
				frame.getTglBtnDonut().setEnabled(false);
				frame.getTglBtnRectangle().setEnabled(false);
				frame.getTglBtnHexagon().setEnabled(false);
				frame.getTglBtnSelect().setEnabled(false);
				frame.getBtnLoad().setEnabled(true);

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public void loadNext () {
		Shape shape = null;
		
		if (counterLog < listLog.size()) {
			String row = listLog.get(counterLog);
			
			if (row.contains("Point")) {
				int x = Integer.parseInt(row.substring(findIndexOf(1, '(', row) + 1, findIndexOf(1, ',', row)));
				int y = Integer.parseInt(row.substring(findIndexOf(1, ',', row) + 2, findIndexOf(1, ')', row)));
				int edgeColor = Integer.parseInt(row.substring(findIndexOf(2, '(', row) + 1, findIndexOf(2, ')', row)));
				shape = new Point(x, y, new Color(edgeColor));
			} else if (row.contains("Line")) {
				int startPointX = Integer.parseInt(row.substring(findIndexOf(1, '(', row) + 1, findIndexOf(1,',', row)));
				int startPointY = Integer.parseInt(row.substring(findIndexOf(1, ',', row) + 2, findIndexOf(1, ')', row)));
				int endPointX = Integer.parseInt(row.substring(findIndexOf(2, '(', row) + 1, findIndexOf(2, ',', row)));
				int endPointY = Integer.parseInt(row.substring(findIndexOf(2, ',', row) + 2, findIndexOf(2, ')', row)));
				int color = Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(3, ')', row)));
				shape = new Line(new Point(startPointX, startPointY), new Point(endPointX, endPointY), new Color(color));
			} else if (row.contains("Rectangle")) {
				int upperLeftPointX = Integer.parseInt(row.substring(findIndexOf(1, '(', row) + 1, findIndexOf(1, ',', row)));
				int upperLeftPointY = Integer.parseInt(row.substring(findIndexOf(1, ',', row) + 2, findIndexOf(1, ')', row)));
				int width = Integer.parseInt(row.substring(findIndexOf(1, '=', row) + 1, findIndexOf(3, ',', row)));
				int height = Integer.parseInt(row.substring(findIndexOf(2, '=', row) + 1, findIndexOf(4, ',', row)));
				int edgeColor = Integer.parseInt(row.substring(findIndexOf(2, '(', row) + 1, findIndexOf(2, ')', row)));
				int innerColor = Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(3, ')', row)));
				shape = new Rectangle(new Point(upperLeftPointX, upperLeftPointY), height, width, new Color(edgeColor), new Color(innerColor));
			} else if (row.contains("Circle")) {
				int centerX = Integer.parseInt(row.substring(findIndexOf(1, '(', row) + 1, findIndexOf(1, ',', row)));
				int centerY = Integer.parseInt(row.substring(findIndexOf(1, ',', row) + 2, findIndexOf(1, ')', row)));
				int radius =  Integer.parseInt(row.substring(findIndexOf(1, '=', row) + 1, findIndexOf(3, ',', row)));
				int edgeColor = Integer.parseInt(row.substring(findIndexOf(2, '(', row) + 1, findIndexOf(2, ')', row)));
				int innerColor = Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(3, ')', row)));
				shape = new Circle(new Point(centerX, centerY), radius, new Color(edgeColor), new Color(innerColor));	
			} else if (row.contains("Donut")) {
				int centerX = Integer.parseInt(row.substring(findIndexOf(1, '(', row) + 1, findIndexOf(1, ',', row)));
				int centerY = Integer.parseInt(row.substring(findIndexOf(1, ',', row) + 2, findIndexOf(1, ')', row)));
				int radius = Integer.parseInt(row.substring(findIndexOf(1, '=', row) + 1, findIndexOf(3, ',', row)));
				int innerRadius = Integer.parseInt(row.substring(findIndexOf(2, '=', row) + 1, findIndexOf(4, ',', row)));
				int edgeColor = Integer.parseInt(row.substring(findIndexOf(2, '(', row) + 1, findIndexOf(2, ')', row)));
				int innerColor = Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(3, ')', row)));
				shape = new Donut(new Point(centerX, centerY), radius, innerRadius, new Color(edgeColor), new Color(innerColor));
			} else if (row.contains("Hexagon")) {
				int centerX = Integer.parseInt(row.substring(findIndexOf(1, '(', row) + 1, findIndexOf(1, ',', row)));
				int centerY = Integer.parseInt(row.substring(findIndexOf(1, ',', row) + 2, findIndexOf(1, ')', row)));
				int radius = Integer.parseInt(row.substring(findIndexOf(1, '=', row) + 1, findIndexOf(3, ',', row)));
				int color = Integer.parseInt(row.substring(findIndexOf(2, '(', row) + 1, findIndexOf(2, ')', row)));
				int innerColor = Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(3, ')', row)));
				shape = new HexagonAdapter(new Point(centerX, centerY), radius, new Color(color), new Color(innerColor));
			}
			
			if (row.contains("Added")) {
				CmdAddShape cmdAddShape;
				
				if(row.contains("Undo")) {
					cmdAddShape = (CmdAddShape) undoStack.peek();
					cmdAddShape.unexecute();
					undoStack.pop();
					redoStack.push(cmdAddShape);
					frame.getTextArea().append("Undo " + cmdAddShape.toString());
				} else if (row.contains("Redo")) {
					cmdAddShape = (CmdAddShape) redoStack.peek();
					cmdAddShape.execute();
					redoStack.pop();
					undoStack.push(cmdAddShape);
					frame.getTextArea().append("Redo " + cmdAddShape.toString());
				} else {
					cmdAddShape = new CmdAddShape(model, shape);
					cmdAddShape.execute();
					undoStack.push(cmdAddShape);
					redoStack.clear();
					frame.getTextArea().append(cmdAddShape.toString());
				}
			}
			if (row.contains("Selected")) {
				CmdSelectShape cmdSelectShape;
				
				if(row.contains("Undo")) {
					cmdSelectShape = (CmdSelectShape) undoStack.peek();
					cmdSelectShape.unexecute();
					undoStack.pop();
					redoStack.push(cmdSelectShape);
					frame.getTextArea().append("Undo " + cmdSelectShape.toString());
				} else if (row.contains("Redo")){
					cmdSelectShape = (CmdSelectShape) redoStack.peek();
					cmdSelectShape.execute();
					redoStack.pop();
					undoStack.push(cmdSelectShape);
					frame.getTextArea().append("Redo " + cmdSelectShape.toString());
				} else {
					shape = model.getShapes().get(model.getShapes().indexOf(shape));
					cmdSelectShape = new CmdSelectShape(this, shape);
					cmdSelectShape.execute();
					undoStack.push(cmdSelectShape);
					redoStack.clear();
					frame.getTextArea().append(cmdSelectShape.toString());
				} 
			} else if (row.contains("Deselected")) {
				CmdDeselectShape cmdDeselectShape;
				
				if(row.contains("Undo")) {
					cmdDeselectShape = (CmdDeselectShape) undoStack.peek();
					cmdDeselectShape.unexecute();
					undoStack.pop();
					redoStack.push(cmdDeselectShape);
					frame.getTextArea().append("Undo " + cmdDeselectShape.toString());
				} else if (row.contains("Redo")) {
					cmdDeselectShape = (CmdDeselectShape) redoStack.peek();
					cmdDeselectShape.execute();
					redoStack.pop();
					undoStack.push(cmdDeselectShape);
					frame.getTextArea().append("Redo " + cmdDeselectShape.toString());
				} else {
					shape = selectedShapes.get(selectedShapes.indexOf(shape));
					cmdDeselectShape = new CmdDeselectShape(this, shape);
					cmdDeselectShape.execute();
					undoStack.push(cmdDeselectShape); //izbacujemo ga iz steka
					redoStack.clear();
					frame.getTextArea().append(cmdDeselectShape.toString());
				}
			}else if (row.contains("Modified")) {
				if (shape instanceof Point) {
					CmdModifyPoint cmdModifyPoint;
					
					if(row.contains("Undo")) {
						cmdModifyPoint = (CmdModifyPoint) undoStack.peek();
						cmdModifyPoint.unexecute();
						undoStack.pop();
						redoStack.push(cmdModifyPoint);
						frame.getTextArea().append("Undo " + cmdModifyPoint.toString());
					} else if (row.contains("Redo")) {
						cmdModifyPoint = (CmdModifyPoint) redoStack.peek();
						cmdModifyPoint.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyPoint);
						frame.getTextArea().append("Redo " + cmdModifyPoint.toString());
					} else {
						shape = selectedShapes.get(0);
						Point newPoint = new Point();
						
						newPoint.setX(Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(3, ',', row))));
						newPoint.setY(Integer.parseInt(row.substring(findIndexOf(3, ',', row) + 2, findIndexOf(3, ')', row))));
						newPoint.setColor(new Color(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(4, ')', row)))));

						cmdModifyPoint = new CmdModifyPoint((Point) shape, newPoint);
						cmdModifyPoint.execute();
						undoStack.push(cmdModifyPoint);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyPoint.toString());
					}
				} else if (shape instanceof Line) {
					CmdModifyLine cmdModifyLine;
					
					if(row.contains("Undo")) {
						cmdModifyLine = (CmdModifyLine) undoStack.peek();
						cmdModifyLine.unexecute();
						undoStack.pop();
						redoStack.push(cmdModifyLine);
						frame.getTextArea().append("Undo " + cmdModifyLine.toString());
					} else if (row.contains("Redo")) {
						cmdModifyLine = (CmdModifyLine) redoStack.peek();
						cmdModifyLine.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyLine);
						frame.getTextArea().append("Redo " + cmdModifyLine.toString());
					} else {
						shape = selectedShapes.get(0);
						Point newStartPoint = new Point();
						Point newEndPoint = new Point();
						
						newStartPoint.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(4, ',', row))));
						newStartPoint.setY(Integer.parseInt(row.substring(findIndexOf(4, ',', row) + 2, findIndexOf(4, ')', row))));
						newEndPoint.setX(Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ',', row))));
						newEndPoint.setY(Integer.parseInt(row.substring(findIndexOf(5, ',', row) + 2, findIndexOf(5, ')', row))));
						
						Line newLine = new Line(newStartPoint, newEndPoint);
						newLine.setColor(new Color(Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)))));

						cmdModifyLine = new CmdModifyLine((Line) shape, newLine);
						cmdModifyLine.execute();
						undoStack.push(cmdModifyLine);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyLine.toString());
					}
				} else if (shape instanceof HexagonAdapter) {
					CmdModifyHexagon cmdModifyHexagon;
					
					if(row.contains("Undo")) {
						cmdModifyHexagon = (CmdModifyHexagon) undoStack.peek();
						cmdModifyHexagon.unexecute(); 
						undoStack.pop();
						redoStack.push(cmdModifyHexagon);
						frame.getTextArea().append("Undo " + cmdModifyHexagon.toString());
					} else if (row.contains("Redo")) {
						cmdModifyHexagon = (CmdModifyHexagon) redoStack.peek();
						cmdModifyHexagon.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyHexagon);
						frame.getTextArea().append("Redo " + cmdModifyHexagon.toString());
					} else {
						shape = selectedShapes.get(0);
						Point center = new Point();
						int radius, edgeColor, innerColor;
						
						center.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(5, ',', row))));
						center.setY(Integer.parseInt(row.substring(findIndexOf(5, ',', row) + 2, findIndexOf(4, ')', row))));
						radius = (Integer.parseInt(row.substring(findIndexOf(2, '=', row) + 1, findIndexOf(7, ',', row))));
						edgeColor = (Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row))));
						innerColor = (Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row))));
						
						HexagonAdapter newHexagon = new HexagonAdapter(center, radius, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyHexagon = new CmdModifyHexagon((HexagonAdapter) shape, newHexagon);
						cmdModifyHexagon.execute();
						undoStack.push(cmdModifyHexagon);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyHexagon.toString());
					}
				} else if (shape instanceof Rectangle) {
					CmdModifyRectangle cmdModifyRectangle;
					
					if (row.contains("Undo")) {
						cmdModifyRectangle = (CmdModifyRectangle) undoStack.peek();
						cmdModifyRectangle.unexecute();
						undoStack.pop();
						redoStack.push(cmdModifyRectangle);
						frame.getTextArea().append(cmdModifyRectangle.toString());
					} else if (row.contains("Redo")) {
						cmdModifyRectangle = (CmdModifyRectangle) redoStack.peek();
						cmdModifyRectangle.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyRectangle);
						frame.getTextArea().append("Redo " + cmdModifyRectangle.toString());
					} else {
						shape = selectedShapes.get(0);
						Point upperLeftPoint = new Point();
						int width, height, edgeColor, innerColor;
						
						upperLeftPoint.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(6, ',', row))));
						upperLeftPoint.setY(Integer.parseInt(row.substring(findIndexOf(6, ',', row) + 2, findIndexOf(4, ')', row))));
						
						width = Integer.parseInt(row.substring(findIndexOf(3, '=', row) + 1, findIndexOf(8, ',', row)));
						height = Integer.parseInt(row.substring(findIndexOf(4, '=', row) + 1, findIndexOf(9, ',', row)));
						
						edgeColor = Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row)));
						innerColor = Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)));						
					
						Rectangle newRectangle = new Rectangle(upperLeftPoint, height, width, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyRectangle = new CmdModifyRectangle((Rectangle) shape, newRectangle);
						cmdModifyRectangle.execute(); 
						undoStack.push(cmdModifyRectangle);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyRectangle.toString());
					}
				} else if (shape instanceof Donut) {
					CmdModifyDonut cmdModifyDonut;
					
					if(row.contains("Undo")) {
						cmdModifyDonut = (CmdModifyDonut) undoStack.peek();
						cmdModifyDonut.unexecute(); 
						undoStack.pop();
						redoStack.push(cmdModifyDonut);
						frame.getTextArea().append("Undo " + cmdModifyDonut.toString());
					} else if (row.contains("Redo")) {
						cmdModifyDonut = (CmdModifyDonut) redoStack.peek();
						cmdModifyDonut.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyDonut);
						frame.getTextArea().append("Redo " + cmdModifyDonut.toString());
					} else {
						shape = selectedShapes.get(0);
						Point center = new Point();
						int radius, innerRadius, edgeColor, innerColor;
						
						center.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(6, ',', row))));
						center.setY(Integer.parseInt(row.substring(findIndexOf(6, ',', row) + 2, findIndexOf(4, ')', row))));
						
						radius = Integer.parseInt(row.substring(findIndexOf(3, '=', row) + 1, findIndexOf(8, ',', row)));
						innerRadius = Integer.parseInt(row.substring(findIndexOf(4, '=', row) + 1, findIndexOf(9, ',', row)));
						
						edgeColor = Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row)));
						innerColor = Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)));
						
						Donut newDonut = new Donut(center, radius, innerRadius, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyDonut = new CmdModifyDonut((Donut) shape, newDonut);
						cmdModifyDonut.execute(); 
						undoStack.push(cmdModifyDonut);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyDonut.toString());	
					}
				} else if (shape instanceof Circle) {
					CmdModifyCircle cmdModifyCircle;
					
					if(row.contains("Undo")) {
						cmdModifyCircle = (CmdModifyCircle) undoStack.peek();
						cmdModifyCircle.unexecute(); 
						undoStack.pop();
						redoStack.push(cmdModifyCircle);
						frame.getTextArea().append("Undo " + cmdModifyCircle.toString());
					} else if (row.contains("Redo")) {
						cmdModifyCircle = (CmdModifyCircle) redoStack.peek();
						cmdModifyCircle.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyCircle);
						frame.getTextArea().append("Redo " + cmdModifyCircle.toString());
					} else {
						shape = selectedShapes.get(0);
						Point center = new Point();
						int radius, edgeColor, innerColor;
						
						center.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(5, ',', row))));
						center.setY(Integer.parseInt(row.substring(findIndexOf(5, ',', row) + 2, findIndexOf(4, ')', row))));
						radius = Integer.parseInt(row.substring(findIndexOf(2, '=', row) + 1, findIndexOf(7, ',', row)));
						edgeColor = Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row)));
						innerColor = Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)));
						
						Circle newCircle = new Circle(center, radius, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyCircle = new CmdModifyCircle((Circle) shape, newCircle);
						cmdModifyCircle.execute(); 
						undoStack.push(cmdModifyCircle);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyCircle.toString());
					}
				}
			} else if (row.contains("Modified")) {
				if (shape instanceof Point) {
					CmdModifyPoint cmdModifyPoint;
					
					if(row.contains("Undo")) {
						cmdModifyPoint = (CmdModifyPoint) undoStack.peek();
						cmdModifyPoint.unexecute();
						undoStack.pop();
						redoStack.push(cmdModifyPoint);
						frame.getTextArea().append("Undo " + cmdModifyPoint.toString());
					} else if (row.contains("Redo")) {
						cmdModifyPoint = (CmdModifyPoint) redoStack.peek();
						cmdModifyPoint.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyPoint);
						frame.getTextArea().append("Redo " + cmdModifyPoint.toString());
					} else {
						shape = selectedShapes.get(0);
						Point newPoint = new Point();
						
						newPoint.setX(Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(3, ',', row))));
						newPoint.setY(Integer.parseInt(row.substring(findIndexOf(3, ',', row) + 2, findIndexOf(3, ')', row))));
						newPoint.setColor(new Color(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(4, ')', row)))));

						cmdModifyPoint = new CmdModifyPoint((Point) shape, newPoint);
						cmdModifyPoint.execute();
						undoStack.push(cmdModifyPoint);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyPoint.toString());
					}
				} else if (shape instanceof Line) {
					CmdModifyLine cmdModifyLine;
					
					if(row.contains("Undo")) {
						cmdModifyLine = (CmdModifyLine) undoStack.peek();
						cmdModifyLine.unexecute();
						undoStack.pop();
						redoStack.push(cmdModifyLine);
						frame.getTextArea().append("Undo " + cmdModifyLine.toString());
					} else if (row.contains("Redo")) {
						cmdModifyLine = (CmdModifyLine) redoStack.peek();
						cmdModifyLine.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyLine);
						frame.getTextArea().append("Redo " + cmdModifyLine.toString());
					} else {
						shape = selectedShapes.get(0);
						Point newStartPoint = new Point();
						Point newEndPoint = new Point();
						
						newStartPoint.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(4, ',', row))));
						newStartPoint.setY(Integer.parseInt(row.substring(findIndexOf(4, ',', row) + 2, findIndexOf(4, ')', row))));
						newEndPoint.setX(Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ',', row))));
						newEndPoint.setY(Integer.parseInt(row.substring(findIndexOf(5, ',', row) + 2, findIndexOf(5, ')', row))));
						
						Line newLine = new Line(newStartPoint, newEndPoint);
						newLine.setColor(new Color(Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)))));

						cmdModifyLine = new CmdModifyLine((Line) shape, newLine);
						cmdModifyLine.execute();
						undoStack.push(cmdModifyLine);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyLine.toString());
					}
				} else if (shape instanceof HexagonAdapter) {
					CmdModifyHexagon cmdModifyHexagon;
					
					if(row.contains("Undo")) {
						cmdModifyHexagon = (CmdModifyHexagon) undoStack.peek();
						cmdModifyHexagon.unexecute(); 
						undoStack.pop();
						redoStack.push(cmdModifyHexagon);
						frame.getTextArea().append("Undo " + cmdModifyHexagon.toString());
					} else if (row.contains("Redo")) {
						cmdModifyHexagon = (CmdModifyHexagon) redoStack.peek();
						cmdModifyHexagon.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyHexagon);
						frame.getTextArea().append("Redo " + cmdModifyHexagon.toString());
					} else {
						shape = selectedShapes.get(0);
						Point center = new Point();
						int radius, edgeColor, innerColor;
						
						center.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(5, ',', row))));
						center.setY(Integer.parseInt(row.substring(findIndexOf(5, ',', row) + 2, findIndexOf(4, ')', row))));
						radius = (Integer.parseInt(row.substring(findIndexOf(2, '=', row) + 1, findIndexOf(7, ',', row))));
						edgeColor = (Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row))));
						innerColor = (Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row))));
						
						HexagonAdapter newHexagon = new HexagonAdapter(center, radius, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyHexagon = new CmdModifyHexagon((HexagonAdapter) shape, newHexagon);
						cmdModifyHexagon.execute();
						undoStack.push(cmdModifyHexagon);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyHexagon.toString());
					}
				} else if (shape instanceof Rectangle) {
					CmdModifyRectangle cmdModifyRectangle;
					
					if (row.contains("Undo")) {
						cmdModifyRectangle = (CmdModifyRectangle) undoStack.peek();
						cmdModifyRectangle.unexecute();
						undoStack.pop();
						redoStack.push(cmdModifyRectangle);
						frame.getTextArea().append(cmdModifyRectangle.toString());
					} else if (row.contains("Redo")) {
						cmdModifyRectangle = (CmdModifyRectangle) redoStack.peek();
						cmdModifyRectangle.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyRectangle);
						frame.getTextArea().append("Redo " + cmdModifyRectangle.toString());
					} else {
						shape = selectedShapes.get(0);
						Point upperLeftPoint = new Point();
						int width, height, edgeColor, innerColor;
						
						upperLeftPoint.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(6, ',', row))));
						upperLeftPoint.setY(Integer.parseInt(row.substring(findIndexOf(6, ',', row) + 2, findIndexOf(4, ')', row))));
						
						width = Integer.parseInt(row.substring(findIndexOf(3, '=', row) + 1, findIndexOf(8, ',', row)));
						height = Integer.parseInt(row.substring(findIndexOf(4, '=', row) + 1, findIndexOf(9, '=', row)));
						
						innerColor = Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row)));
						edgeColor = Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)));						
					
						Rectangle newRectangle = new Rectangle(upperLeftPoint, width, height, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyRectangle = new CmdModifyRectangle((Rectangle) shape, newRectangle);
						cmdModifyRectangle.execute(); 
						undoStack.push(cmdModifyRectangle);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyRectangle.toString());
					}
				} else if (shape instanceof Donut) {
					CmdModifyDonut cmdModifyDonut;
					
					if(row.contains("Undo")) {
						cmdModifyDonut = (CmdModifyDonut) undoStack.peek();
						cmdModifyDonut.unexecute(); 
						undoStack.pop();
						redoStack.push(cmdModifyDonut);
						frame.getTextArea().append("Undo " + cmdModifyDonut.toString());
					} else if (row.contains("Redo")) {
						cmdModifyDonut = (CmdModifyDonut) redoStack.peek();
						cmdModifyDonut.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyDonut);
						frame.getTextArea().append("Redo " + cmdModifyDonut.toString());
					} else {
						shape = selectedShapes.get(0);
						Point center = new Point();
						int radius, innerRadius, edgeColor, innerColor;
						
						center.setX(Integer.parseInt(row.substring(findIndexOf(4, '(', row) + 1, findIndexOf(6, ',', row))));
						center.setY(Integer.parseInt(row.substring(findIndexOf(6, ',', row) + 2, findIndexOf(4, ')', row))));
						
						radius = Integer.parseInt(row.substring(findIndexOf(3, '=', row) + 1, findIndexOf(8, ',', row)));
						innerRadius = Integer.parseInt(row.substring(findIndexOf(4, '=', row) + 1, findIndexOf(9, ',', row)));
						
						edgeColor = Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row)));
						innerColor = Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)));
						
						Donut newDonut = new Donut(center, radius, innerRadius, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyDonut = new CmdModifyDonut((Donut) shape, newDonut);
						cmdModifyDonut.execute(); 
						undoStack.push(cmdModifyDonut);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyDonut.toString());	
					}
				} else if (shape instanceof Circle) {
					CmdModifyCircle cmdModifyCircle;
					
					if(row.contains("Undo")) {
						cmdModifyCircle = (CmdModifyCircle) undoStack.peek();
						cmdModifyCircle.unexecute(); 
						undoStack.pop();
						redoStack.push(cmdModifyCircle);
						frame.getTextArea().append("Undo " + cmdModifyCircle.toString());
					} else if (row.contains("Redo")) {
						cmdModifyCircle = (CmdModifyCircle) redoStack.peek();
						cmdModifyCircle.execute(); 
						redoStack.pop();
						undoStack.push(cmdModifyCircle);
						frame.getTextArea().append("Redo " + cmdModifyCircle.toString());
					} else {
						shape = selectedShapes.get(0);
						Point center = new Point();
						int radius, edgeColor, innerColor;
						
						center.setX(Integer.parseInt(row.substring(findIndexOf(3, '(', row) + 1, findIndexOf(5, ',', row))));
						center.setY(Integer.parseInt(row.substring(findIndexOf(5, ',', row) + 2, findIndexOf(3, ')', row))));
						radius = Integer.parseInt(row.substring(findIndexOf(2, '=', row) + 1, findIndexOf(7, ',', row) + 1));
						edgeColor = Integer.parseInt(row.substring(findIndexOf(5, '(', row) + 1, findIndexOf(5, ')', row)));
						innerColor = Integer.parseInt(row.substring(findIndexOf(6, '(', row) + 1, findIndexOf(6, ')', row)));
						
						Circle newCircle = new Circle(center, radius, new Color(edgeColor), new Color(innerColor));
						
						cmdModifyCircle = new CmdModifyCircle((Circle) shape, newCircle);
						cmdModifyCircle.execute(); 
						undoStack.push(cmdModifyCircle);
						redoStack.clear();
						frame.getTextArea().append(cmdModifyCircle.toString());
					}
				}
			} else if (row.contains("Removed")) {
				CmdRemoveShape cmdRemoveShape;
				
				if(row.contains("Undo")) {
					cmdRemoveShape = (CmdRemoveShape) undoStack.peek();
					cmdRemoveShape.unexecute();
					redoShapesList.add(undoShapesList.get(undoShapesList.size() - 1));
					selectedShapes.add(undoShapesList.get(undoShapesList.size() - 1));
					undoShapesList.remove(undoShapesList.size() - 1);
					undoStack.pop();
					redoStack.push(cmdRemoveShape);
					frame.getTextArea().append("Undo " + cmdRemoveShape.toString());
				} else if (row.contains("Redo")) {
					cmdRemoveShape = (CmdRemoveShape) redoStack.peek();
					cmdRemoveShape.execute();
					undoShapesList.add(redoShapesList.get(redoShapesList.size() - 1));
					selectedShapes.remove(redoShapesList.get(redoShapesList.size() - 1));
					redoShapesList.remove(redoShapesList.size() - 1);
					redoStack.pop();
					undoStack.push(cmdRemoveShape);
					frame.getTextArea().append("Redo " + cmdRemoveShape.toString());
				} else {
					shape = selectedShapes.get(0);
					cmdRemoveShape = new CmdRemoveShape(model, shape, model.getShapes().indexOf(shape));
					cmdRemoveShape.execute();
					selectedShapes.remove(shape);
					undoShapesList.add(shape);
					undoStack.push(cmdRemoveShape);
					redoStack.clear();
					frame.getTextArea().append(cmdRemoveShape.toString());
				}
			} else if (row.contains("Moved to back")) {
				CmdToBack cmdToBack;
				
				if (row.contains("Undo")) {
					cmdToBack = (CmdToBack) undoStack.peek();
					cmdToBack.unexecute();
					undoStack.pop();
					redoStack.push(cmdToBack);
					frame.getTextArea().append("Undo " + cmdToBack.toString());
				} else if (row.contains("Redo")) {
					cmdToBack = (CmdToBack) redoStack.peek();
					cmdToBack.execute();
					redoStack.pop();
					undoStack.push(cmdToBack);
					frame.getTextArea().append("Redo " + cmdToBack.toString());
				} else {
					shape = selectedShapes.get(0);
					cmdToBack = new CmdToBack(model, shape);
					cmdToBack.execute(); 
					undoStack.push(cmdToBack);
					redoStack.clear();
					frame.getTextArea().append(cmdToBack.toString());
				}
			} else if (row.contains("Moved to front")) {
				CmdToFront cmdToFront;
				
				if(row.contains("Undo")) {
					cmdToFront = (CmdToFront) undoStack.peek();
					cmdToFront.unexecute(); 
					undoStack.pop();
					redoStack.push(cmdToFront);
					frame.getTextArea().append("Undo " + cmdToFront.toString());
				} else if (row.contains("Redo")) {
					cmdToFront = (CmdToFront) redoStack.peek();
					cmdToFront.execute(); 
					redoStack.pop();
					undoStack.push(cmdToFront);
					frame.getTextArea().append("Redo " + cmdToFront.toString());
				} else {
					shape = selectedShapes.get(0);
					cmdToFront = new CmdToFront(model, shape);
					cmdToFront.execute(); 
					undoStack.push(cmdToFront);
					redoStack.clear();
					frame.getTextArea().append(cmdToFront.toString());
				}
			} else if (row.contains("Bringed to back")) {
				CmdBringToBack cmdBringToBack;

				if (row.contains("Undo")) {
					cmdBringToBack = (CmdBringToBack) undoStack.peek();
					cmdBringToBack.unexecute();
					undoStack.pop();
					redoStack.push(cmdBringToBack);
					frame.getTextArea().append("Undo " + cmdBringToBack.toString());
				} else if (row.contains("Redo")) {
					cmdBringToBack = (CmdBringToBack) redoStack.peek();
					cmdBringToBack.execute();
					redoStack.pop();
					undoStack.push(cmdBringToBack);
					frame.getTextArea().append("Redo " + cmdBringToBack.toString());
				} else {
					shape = selectedShapes.get(0);
					cmdBringToBack = new CmdBringToBack(model, shape);
					cmdBringToBack.execute();
					undoStack.push(cmdBringToBack);
					redoStack.clear();
					frame.getTextArea().append(cmdBringToBack.toString());
				}
			} else if (row.contains("Bringed to front")) {
				CmdBringToFront cmdBringToFront;

				if (row.contains("Undo")) {
					cmdBringToFront = (CmdBringToFront) undoStack.peek();
					cmdBringToFront.unexecute();
					undoStack.pop();
					redoStack.push(cmdBringToFront);
					frame.getTextArea().append("Undo " + cmdBringToFront.toString());
				} else if (row.contains("Redo")) {
					cmdBringToFront = (CmdBringToFront) redoStack.peek();
					cmdBringToFront.execute();
					redoStack.pop();
					undoStack.push(cmdBringToFront);
					frame.getTextArea().append("Redo " + cmdBringToFront.toString());
				} else {
					shape = selectedShapes.get(0);
					cmdBringToFront = new CmdBringToFront(model, shape);
					cmdBringToFront.execute();
					undoStack.push(cmdBringToFront);
					redoStack.clear();
					frame.getTextArea().append(cmdBringToFront.toString());
				}
			} 
			
			counterLog++;
			frame.getView().repaint();
		} else {
			frame.getBtnLoad().setEnabled(false);
			visibleButtons();
			frame.getTglBtnPoint().setEnabled(true);
			frame.getTglBtnLine().setEnabled(true);
			frame.getTglBtnCircle().setEnabled(true);
			frame.getTglBtnDonut().setEnabled(true);
			frame.getTglBtnRectangle().setEnabled(true);
			frame.getTglBtnHexagon().setEnabled(true);
			frame.getBtnUndo().setEnabled(false);
		}
	}
	
	public int findIndexOf(int n, char c, String s) {
        int occurr = 0;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == c) {
                occurr += 1;
            }
            if(occurr == n) {
                return i;
            }
        }
        return -1;
    }

}
