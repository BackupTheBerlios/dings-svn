/*
 * AEditView.java
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

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.model.Toolbox;
import net.vanosten.dings.swing.DingsSwingConstants;
import net.vanosten.dings.uiif.IDetailsView;

public abstract class AEditView extends AViewWithScrollPane implements IDetailsView, KeyListener {
	private JButton applyB, revertB, deleteB, doneB;

	/** Whether the delete button should be shown */
	private boolean showDelete = false;

	/** Whether the done button should be shown */
	private boolean showDone = false;

	/** The message for ok */
	private String msgDone;

	/**
	 * Indicates whether the gui value is changed programmatically
	 * or by the user. This is important, as otherwise the programmatic
	 * changes would again call the method (endless loop).
	 * The value is true when programmatically updated.
	*/
	protected boolean isUpdating = false;

	public AEditView(String aTitle, ComponentOrientation aComponentOrientation, boolean aShowDelete, boolean aShowDone, String aMsgDone) {
		super(aTitle, aComponentOrientation);
		this.showDelete = aShowDelete;
		this.showDone = aShowDone;
		this.msgDone = aMsgDone;
		initializeGUI();
		this.setGUIOrientation();
	} //END public AEditView(String, ComponentOrientation, ...)

	protected abstract void initializeEditP();

	//Implements AViewWithButtons
	protected void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));

		JPanel myButtonsP = new JPanel();
		int countButtons = 2;
		if (showDelete) {
			countButtons++;
		}
		if (showDone) {
			countButtons++;
		}
		myButtonsP.setLayout(new GridLayout(1,countButtons, DingsSwingConstants.SP_H_C, 0));

		myButtonsP.add(applyB);
		myButtonsP.add(revertB);
		if (showDelete) {
			myButtonsP.add(deleteB);
		}
		if (showDone) {
			myButtonsP.add(doneB);
		}

		buttonsP.add(myButtonsP);
	} //END private void initializeButtonP()

	//implements AViewWithButtons
	protected final void initButtonComponents() {
		applyB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.apply")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_APPLY_BTN, "FIXME"));
		applyB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.apply").charAt(0));
		applyB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onApply();
			}
		});

		revertB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.revert")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_RESET_BTN, "FIXME"));
		revertB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.revert").charAt(0));
		revertB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onRevert();
			}
		});

		if (showDelete) {
			deleteB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.delete")
					, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_DELETE_BTN, "FIXME"));
			deleteB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.delete").charAt(0));
			deleteB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					onDelete();
				}
			});
		}
		if (showDone) {
			doneB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.back")
					, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_BACK_BTN, "FIXME"));
			doneB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.back").charAt(0));
			doneB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					onDone();
				}
			});
		}
	} //END protected final void initButtonComponents()

	//implements IDetailsView
	public void setEditing(boolean isEditing, boolean isValid) {
		if (true == isEditing && true == isValid) {
			applyB.setEnabled(true);
		} else {
			applyB.setEnabled(false);
		}
		revertB.setEnabled(isEditing);

		if (showDone) {
			boolean reverse = true;
			if (true == isEditing) {
				reverse = false;
			}
			doneB.setEnabled(reverse);
		}
		//the deleteB is always active!
	} //END private void setEditing(boolean)

	//Overrides AViewWithButtons
	public boolean init(IAppEventHandler aController) {
		this.controller = aController;
		if (null == controller) return false;
		onRevert();
		return true;
	} //END public boolean init(IAppEventHandler)

	//-----------------------------------------------------------------------

	private void onApply() {
		AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
		ape.setMessage(MessageConstants.D_EDIT_VIEW_APPLY);
		controller.handleAppEvent(ape);
	} //END private void onApply()

	private void onRevert() {
		AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
		ape.setMessage(MessageConstants.D_EDIT_VIEW_REVERT);
		controller.handleAppEvent(ape);
	} //END private void onRevert()

	private void onDelete() {
		AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
		ape.setMessage(MessageConstants.D_EDIT_VIEW_DELETE);
		controller.handleAppEvent(ape);
	} //END private void onDelete()

	private void onDone() {
		AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
		ape.setMessage(msgDone);
		controller.handleAppEvent(ape);
	} //END private void onDone()

	protected void onChange() {
		if (!isUpdating) {
			//check for real changes
			AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
			ape.setMessage(MessageConstants.D_EDIT_VIEW_CHECK_CHANGE);
			controller.handleAppEvent(ape);
		}
	} //END protected void onChange()

	//-----------------implement KeyListener --------------------

	//implements KeyListener
	public void keyTyped(KeyEvent evt) {
		//nothing to be done
	} //END public void keyTyped(KeyEvent)

	//implements KeyListener
	public void keyReleased(KeyEvent evt) {
		onChange();
	} //END public void keyReleased(KeyEvent)

	//implements KeyListener
	public void keyPressed(KeyEvent evt) {
		//nothing to be done
	} //END public void keyPressed(KeyEvent)

} //END public abstract class AEditView extends AViewWithScrollPane
