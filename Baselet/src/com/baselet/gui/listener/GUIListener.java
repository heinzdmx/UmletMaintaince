package com.baselet.gui.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.SharedConstants;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Command;
import com.baselet.diagram.command.Macro;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;

public class GUIListener implements KeyListener {

	private static final int SIZE_CHANGE_STEP = 3;

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			SharedConstants.stickingEnabled = false;
		}

		DiagramHandler handler = Main.getInstance().getDiagramHandler();

		if (handler != null && !e.isAltDown() && !e.isAltGraphDown() /* && !e.isControlDown() && !e.isMetaDown() */) {

			Collection<GridElement> selectedEntities = handler.getDrawPanel().getSelector().getSelectedElements();
			int changeElementSize = 0;

			/**
			 * Enter: jumps directly into the diagram
			 */
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				Main.getInstance().getGUI().focusPropertyPane();
			}

			/**
			 * Ctrl +/-: Zoom diagram by 10%
			 */
			// KeyChar check doesn't check non-numpad + on some keyboards, therefore we also need KeyEvent.VK_PLUS
			else if (e.getKeyChar() == '+' || e.getKeyCode() == KeyEvent.VK_PLUS) {
				if (selectedEntities.isEmpty()) {
					int actualZoom = handler.getGridSize();
					handler.setGridAndZoom(actualZoom + 1);
				}
				else {
					changeElementSize = SIZE_CHANGE_STEP;
				}
			}

			// KeyChar check doesn't check non-numpad - on some keyboards, therefore we also need KeyEvent.VK_MINUS
			else if (e.getKeyChar() == '-' || e.getKeyCode() == KeyEvent.VK_MINUS) {
				if (selectedEntities.isEmpty()) {
					int actualZoom = handler.getGridSize();
					handler.setGridAndZoom(actualZoom - 1);
				}
				else {
					changeElementSize = -SIZE_CHANGE_STEP;
				}
			}

			/**
			 * Cursors: Move diagram by a small distance
			 */
			else {
				int diffx = 0;
				int diffy = 0;

				if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN) {
					diffy = handler.getGridSize();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP) {
					diffy = -handler.getGridSize();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT) {
					diffx = -handler.getGridSize();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT) {
					diffx = handler.getGridSize();
				}

				if (diffx != 0 || diffy != 0) {
					// Move only selected entities or all if no entity is selected
					Collection<GridElement> entitiesToBeMoved = selectedEntities.isEmpty() ? handler.getDrawPanel().getGridElements() : selectedEntities;

					Point opos = getOriginalPos(diffx, diffy, entitiesToBeMoved.iterator().next());
					Vector<Command> ALL_MOVE_COMMANDS = GridElementListener.calculateFirstMoveCommands(diffx, diffy, opos, entitiesToBeMoved, e.isShiftDown(), true, handler, Collections.<Direction> emptySet());
					handler.getController().executeCommand(new Macro(ALL_MOVE_COMMANDS));
					Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
				}
			}

			if (changeElementSize != 0) {
				for (GridElement element : selectedEntities) {
					element.changeSize(changeElementSize, changeElementSize);
				}
			}
		}
	}

	private Point getOriginalPos(int diffx, int diffy, GridElement ge) {
		return new Point(ge.getRectangle().x - diffx, ge.getRectangle().y - diffy);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			SharedConstants.stickingEnabled = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

}
