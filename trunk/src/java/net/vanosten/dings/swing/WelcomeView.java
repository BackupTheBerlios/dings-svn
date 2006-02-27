/*
 * WelcomeView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 2005 Rick Gruber-Riemer (rick@vanosten.net)
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

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.model.Toolbox;

import net.vanosten.dings.uiif.IWelcomeView;

public class WelcomeView extends AViewWithButtons implements IWelcomeView {
	private final static long serialVersionUID = 1L;

	private JButton newB, openB;

	public WelcomeView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.welcome"), aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //END public WelcomeView(ComponentOrientation)
	
	//implements AViewWithButtons
	protected void initializeMainP() {
		mainP = new JPanel();	
		GridBagLayout mgbl = new GridBagLayout();
		GridBagConstraints mgbc = new GridBagConstraints();
		mainP.setLayout(mgbl);
		
		JLabel label = new JLabel(Toolbox.getInstance().getLocalizedString("text.welcome_view.howto"));

		//make the gui
		mgbc.weightx = 1.0;
		mgbc.weighty = 1.0;
		mgbc.fill = GridBagConstraints.HORIZONTAL;
		mgbc.anchor = GridBagConstraints.PAGE_START;
		mgbl.setConstraints(label, mgbc);
		mainP.add(label);
	} //END protected void initializeMainP()
	
	//implements AViewWithButtons
	protected final void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		
		JPanel myButtonsP = new JPanel();
		myButtonsP.setLayout(new GridLayout(1,2, DingsSwingConstants.SP_H_C, 0));
		
		myButtonsP.add(newB);
		myButtonsP.add(openB);
		
		buttonsP.add(myButtonsP);
	} //END protected final void initializeButtonP()
	
	//implements AViewWithButtons
	protected final void initButtonComponents() {
		newB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.new")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_NEW_BTN, "FIXME"));
		newB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.new").charAt(0));
		newB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_NEW_VOCABULARY);
				controller.handleAppEvent(ape);
			}
		});

		openB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.open")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_OPEN_BTN, "FIXME"));
		openB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.open").charAt(0));
		openB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_OPEN_VOCABULARY);
				controller.handleAppEvent(ape);
			}
		});
	} //END protected final void initButtonComponents();
} //END public class WelcomeView extends AViewWithButtons implements IWelcomeView
