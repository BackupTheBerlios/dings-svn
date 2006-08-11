/*
 * InsetAccentbuttonPanel.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
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
package net.vanosten.dings.swing.helperui;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * 
 * A Panel of buttons, each of which inserts its label to a text component
 * at the current caret position.
 * The buttons are grouped into visually distinct groups reflecting the array structure
 * of the characters input parameter in Constructor.
 *
 */
public class InsertCharacterButtonPanel extends JPanel {
	private final static long serialVersionUID = 1L;
	
	/**
	 * @param aTextArea
	 * @param characters a two dinensional array of Strings, where characters are grouped
	 */
	public InsertCharacterButtonPanel(JTextComponent textComponent, String[][] characters) {
		constructPanel(textComponent, characters);
	}
	
	private void constructPanel(JTextComponent textComponent, String[][] characters) {
		//make the gui
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);		
		GroupLayout.SequentialGroup horizontal = layout.createSequentialGroup();
		GroupLayout.ParallelGroup vertical = layout.createParallelGroup();
		
		List<JButton> buttons = new ArrayList<JButton>();
		JButton tempB;
		for (int i = 0; i < characters.length; i++) {
			if (0 != i) {
				horizontal.addPreferredGap(LayoutStyle.UNRELATED);
			}
			for (int j = 0; j < characters[i].length; j++) {
				if (0 != j) {
					horizontal.addPreferredGap(LayoutStyle.RELATED);
				}
				tempB = createButton(characters[i][j], textComponent);
				buttons.add(tempB);
				horizontal.add(tempB);
				vertical.add(tempB);
			}
		}
		//make sure that all buttons have the same width
		Component[] components = new Component[buttons.size()];
		for (int i = 0; i < buttons.size(); i++) {
			components[i] = buttons.get(i);
		}
		layout.linkSize(components, GroupLayout.VERTICAL);
		
		//add groups
		layout.setHorizontalGroup(horizontal);
		layout.setVerticalGroup(vertical);
	}
	
	private JButton createButton(final String label, final JTextComponent textComponent) {
		JButton aButton = new JButton(label);
		aButton.setMargin(new Insets(0,0,0,0));
		aButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				insertCharacter(label, textComponent);
			}
		});
		return aButton;
	}
	
	/**
	 * Inserts the label text of the pressed button into the text
	 * in the text component at the current caret position.
	 * If the text in the text component is null, then the text is set 
	 * tothe buttons label text.
	 * @param aButton
	 */
	private static void insertCharacter(String label, JTextComponent textComponent) {
		String text = textComponent.getText();
		if (null == text) {
			text = label;
		} else {
			int pos = textComponent.getCaretPosition();
			StringBuilder sb = new StringBuilder(text);
			sb.insert(pos, label);
			text = sb.toString();
		}
		textComponent.setText(text);
	}

}
