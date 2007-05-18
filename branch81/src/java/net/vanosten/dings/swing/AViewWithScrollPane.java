/*
 * AViewWithScrollPane.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 05, 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.vanosten.dings.swing;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import java.awt.AWTKeyStroke;
import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AViewWithScrollPane extends AViewWithButtons {

	/** The central panel with the widgets for editing */
	protected JPanel editP;

	public AViewWithScrollPane(String aTitle, ComponentOrientation aComponentOrientation) {
		super(aTitle, aComponentOrientation);
	} //END public AViewWithScrollPane(String, ComponentOrientation)

	//Implements AEditView
	protected void initializeMainP() {
		mainP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		mainP.setLayout(gbl);

		//editP
		editP = new JPanel();
		EmptyBorder border = new EmptyBorder(
			DingsSwingConstants.SP_D_TOP
			, DingsSwingConstants.SP_D_LEFT
			, DingsSwingConstants.SP_D_BUTTOM
			, DingsSwingConstants.SP_D_RIGHT
		);
		editP.setBorder(border);
		initializeEditP();
		JScrollPane scrollP = new JScrollPane(editP);

		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbl.setConstraints(scrollP, gbc);
		mainP.add(scrollP);
	} //END protected void initializeMainP()

	protected abstract void initializeEditP();
	
	//----------------- tabbing using tab instead of CTRL-TAB in JTextArea
	//taken from http://www.javalobby.org/java/forums/t20457.html
	//Swing: Tabbing over JTextArea (whilst still allow tab to be typed)
	public static void invertFocusTraversalBehaviour(JTextArea textArea) {
		Set<AWTKeyStroke> forwardKeys  = textArea.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> backwardKeys = textArea.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
 
		// check that we WANT to modify current focus traversal keystrokes
		if (forwardKeys.size() != 1 || backwardKeys.size() != 1) return;
		final AWTKeyStroke fks = forwardKeys.iterator().next();
		final AWTKeyStroke bks = backwardKeys.iterator().next();
		final int fkm = fks.getModifiers();
		final int bkm = bks.getModifiers();
		final int ctrlMask      = KeyEvent.CTRL_MASK+KeyEvent.CTRL_DOWN_MASK;
		final int ctrlShiftMask = KeyEvent.SHIFT_MASK+KeyEvent.SHIFT_DOWN_MASK+ctrlMask;
		if (fks.getKeyCode() != KeyEvent.VK_TAB || (fkm & ctrlMask) == 0 || (fkm & ctrlMask) != fkm) {
			// not currently CTRL+TAB for forward focus traversal
			return;
		}
		if (bks.getKeyCode() != KeyEvent.VK_TAB || (bkm & ctrlShiftMask) == 0 || (bkm & ctrlShiftMask) != bkm) {
			// not currently CTRL+SHIFT+TAB for backward focus traversal
			return;
		}
 
		// bind our new forward focus traversal keys
		Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(1);
		newForwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB,0));
		textArea.setFocusTraversalKeys(
			KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
			Collections.unmodifiableSet(newForwardKeys)
		);
		// bind our new backward focus traversal keys
		Set<AWTKeyStroke> newBackwardKeys = new HashSet<AWTKeyStroke>(1);
		newBackwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK+KeyEvent.SHIFT_DOWN_MASK));
		textArea.setFocusTraversalKeys(
			KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
			Collections.unmodifiableSet(newBackwardKeys)
		);
 
		// Now, it's still useful to be able to type TABs in some cases.
		// Using this technique assumes that it's rare however (if the user
		// is expected to want to type TAB often, consider leaving text area's
		// behaviour unchanged...).  Let's add some key bindings, inspired
		// from a popular behaviour in instant messaging applications...
		TextInserter.applyTabBinding(textArea);
 
		// we could do the same stuff for RETURN and CTRL+RETURN for activating
		// the root pane's default button: omitted here for brevity
	}

	public static class TextInserter extends AbstractAction {
		static final long serialVersionUID = 1;
		
		private JTextArea textArea;
		private String insertable;
 
		private TextInserter(JTextArea textArea, String insertable) {
			this.textArea   = textArea;
			this.insertable = insertable;
		}
 
		public static void applyTabBinding(JTextArea textArea) {
			textArea.getInputMap(JComponent.WHEN_FOCUSED)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.CTRL_MASK+KeyEvent.CTRL_DOWN_MASK),"tab");
				textArea.getActionMap().put("tab",new TextInserter(textArea, "\t"));
		}
 
		public void actionPerformed(ActionEvent evt) {
			// could be improved to overtype selected range
			textArea.insert(insertable,textArea.getCaretPosition());
		}
	}
} //END public abstract class AViewWithScrollPane
