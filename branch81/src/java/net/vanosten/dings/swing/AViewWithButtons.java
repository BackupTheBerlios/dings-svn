/*
 * AViewWithButtons.java
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

import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JLabel;

import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.uiif.IView;

public abstract class AViewWithButtons extends JPanel implements IView {
	protected JPanel mainP;
	protected JPanel buttonsP;

	private JLabel titleL;

	/** The title of this list */
	private String title;

	/** The component/gui orientation for international apps */
	protected ComponentOrientation guiOrientation;

	protected IAppEventHandler controller;

	public AViewWithButtons(String aTitle, ComponentOrientation aComponentOrientation) {
		super.setComponentOrientation(aComponentOrientation);
		this.title = aTitle;
		this.guiOrientation = aComponentOrientation;
	} //END public AViewWithButtons(String, ComponentOrientation)

	/**
	 * Sets up the initial GUI.
	 */
	protected void initializeGUI() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);
		EmptyBorder border = new EmptyBorder(
			DingsSwingConstants.SP_D_TOP
			, DingsSwingConstants.SP_D_LEFT
			, DingsSwingConstants.SP_D_BUTTOM
			, DingsSwingConstants.SP_D_RIGHT
		);
		this.setBorder(border);

		initButtonComponents();
		initializeButtonP();

		initializeMainP();

		titleL = new JLabel(title);
		titleL.setFont(DingsSwingConstants.TITLE_ONE_FONT);
		titleL.setEnabled(false);

		//make gui
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(titleL, gbc);
		add(titleL);
		//----
		gbc.gridy = 1;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1.0d;
		gbc.weighty = 1.0d;
		gbl.setConstraints(mainP, gbc);
		add(mainP);
		//------
		gbc.gridy = 2;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_COM, 0,0,0);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LAST_LINE_END;
		gbc.weightx = 0.0d;
		gbc.weighty = 0.0d;
		gbl.setConstraints(buttonsP, gbc);
		add(buttonsP);
	} //END private void initializeGUI()

	/**
	 * Sets the main part of the view up.
	 */
	protected abstract void initializeMainP();

	/**
	 * Sets the panel with the buttons up.
	 */
	protected abstract void initializeButtonP();

	/**
	 * Initializes the buttons for the button panel
	 */
	protected abstract void initButtonComponents();

	/**
	 * Initialize the view.
	 *
	 * @param aController - the controller (and model) of this view
	 */
	public boolean init(IAppEventHandler aController) {
		this.controller = aController;
		if (null == controller) return false;
		return true;
	} //END public boolean init()

	/**
	 * Sets the component orientation according to the guiOrientation variable.
	 * The method has to be called as the last one in the constructor of
	 * subcalsses after all GUI related stuff has been initialized.
	 * It is a convenience method instead of calling
	 * <code>this.applyComponentOrientation(this.guiOrientation)</code>
	 */
	protected final void setGUIOrientation() {
		this.applyComponentOrientation(this.guiOrientation);
	} //END protected final void setGUIOrientation()

	/**
	 * (Se)sets the title of the view
	 * @param title
	 */
	protected final void setViewTitle(String title) {
		this.title = title;
		if (null != titleL) {
			titleL.setText(title);
		}
	} //ENd protected final void setViewTitle(String)
} //END public abstract class AViewWithButtons
