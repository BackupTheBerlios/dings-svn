/*
 * ListView.java
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.MessageConstants.Message;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.swing.helperui.IDTableModel;
import net.vanosten.dings.swing.helperui.LevelTableCellRenderer;
import net.vanosten.dings.uiif.IListView;

public abstract class ListView extends JPanel implements IListView {
	private JTable listT;
	private IDTableModel model;
	private JPanel buttonsP, mainP;
	private JButton newB, deleteB, editB;
	private IAppEventHandler controller;

	/** The message for edit */
	protected Message msgEdit;

	/** The title of this list */
	private String title;

	/**
	 * Empty constructor the EntriesListView
	 */
	public ListView(String aTitle, ComponentOrientation aComponentOrientation) {
		super();
		super.setComponentOrientation(aComponentOrientation);
		this.title = aTitle;
		setMessages();
		initializeGUI();
		this.applyComponentOrientation(aComponentOrientation);
	} //END public ListView(String, ComponentOrientation)

	/**
	 * Sets up the initial GUI.
	 */
	private void initializeGUI() {
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

		mainP = new JPanel();
		initializeMainP();

		JLabel titleL = new JLabel(title);
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

	private void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));

		JPanel myButtonsP = new JPanel();
		myButtonsP.setLayout(new GridLayout(1,3, DingsSwingConstants.SP_H_C, 0));

		myButtonsP.add(newB);
		myButtonsP.add(deleteB);
		myButtonsP.add(editB);

		buttonsP.add(myButtonsP);
	} //END private void initializeButtonP()

	private final void initButtonComponents() {
		newB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.add")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_ADD_BTN, "FIXME"));
		newB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.add").charAt(0));
		newB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onNew();
			}
		});

		deleteB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.remove")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_REMOVE_BTN, "FIXME"));
		deleteB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.remove").charAt(0));
		deleteB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Long rowID = getSelectedRowID();
				if (rowID != null) {
					onDelete();
				}
			}
		});

		editB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.edit")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EDIT_BTN, "FIXME"));
		editB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.edit").charAt(0));
		editB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onEdit();
			}
		});
	} //END private final void initButtonComponents()

	private void initializeMainP() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		mainP.setLayout(gbl);

		listT = new JTable();
		listT.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = listT.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				//do check in special method
				onTableSelection(e);
			}
		});
		listT.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					onEdit();
				}
			}
			public void mouseEntered(MouseEvent evt) {
				//nothing to be done
			}
			public void mouseExited(MouseEvent evt) {
				//nothing to be done
			}
			public void mousePressed(MouseEvent evt) {
				//nothing to be done
			}
			public void mouseReleased(MouseEvent evt) {
				//nothing to be done
			}
		});
		listT.setDefaultRenderer(Integer.class, new LevelTableCellRenderer());


		JScrollPane listPane = new JScrollPane(listT);

		//put together
		gbc.weightx = 1.0d;
		gbc.weighty = 1.0d;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(listPane, gbc);
		mainP.add(listPane);
	} //END private initializeMainP()

	/**
	 * Gets the ID of the selected row
	 *
	 * return Long - the ID of the selcted row.
	 */
	private Long getSelectedRowID() {
		return model.getIDAt(listT.getSelectedRow());
	}


	/**
	 * Updates the entries in the list and selects one item
	 */
	public boolean init(IAppEventHandler aController) {
		this.controller = aController;
		if (null == controller) return false;
		AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
		ape.setMessage(MessageConstants.Message.D_LIST_VIEW_REFRESH);
		controller.handleAppEvent(ape);
		return true;
	} //END public boolean init()

	public void setList(String theTitles[], Object theData[][], boolean[] columnFixedWidth) {
		model = new IDTableModel(theTitles, theData);
		listT.setModel(model);
		setTableColumnFixedWidths(columnFixedWidth);

		//enable/disable buttons
		if (0 == model.getRowCount()) {
			deleteB.setEnabled(false);
			editB.setEnabled(false);
		}
		else {
			deleteB.setEnabled(true);
			editB.setEnabled(true);
		}
		listT.requestFocus();
	} //END public void setList(String[], String[], Object[][], boolean[])

	public void setSelected(Long anID) {
		int row = model.getIDIndexPos(anID);
		if (0 <= row) {
			listT.setRowSelectionInterval(row, row);
			listT.scrollRectToVisible(listT.getCellRect(row, 0, true));
		}
	}

	/**
	 * Sets the width of the table columns. Gets called from setList(...)
	 */
	private void setTableColumnFixedWidths(boolean[] fixed) {
		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;

		TableCellRenderer headerRenderer = listT.getTableHeader().getDefaultRenderer();
		for (int i = 0; i < fixed.length; i++) {
			//get column
			column = listT.getColumnModel().getColumn(i);
			comp = headerRenderer.getTableCellRendererComponent(
									 null, column.getHeaderValue(),
									 false, false, 0, 0);
			if (fixed[i]) {
				headerWidth = comp.getPreferredSize().width + 4;
				//Set the width. Need to set min and max also!
				column.setPreferredWidth(headerWidth);
				column.setMinWidth(headerWidth);
				column.setMaxWidth(headerWidth);
			}
			column.setResizable(!fixed[i]);
		}
	} //END private void setTableColumnFixedWidths()

	/**
	 * Assigns the concrete messages.
	 */
	protected abstract void setMessages();

	/**
	 * Override this method tho return an appropriate Id for the detail of a new item.
	 * Currently only used for entries.
	 * If null is returned, then this means that no new item should be created.
	 *
	 * @return String - the id of
	 */
	protected Long getIdTypeForNew() {
		return Constants.UNDEFINED_ID;
	} //END protected String getIdTypeForNew()

	//-----------------------------------------------------------------------

	/**
	 * Requests a new entry by asking, which entry type that should be chosen.
	 */
	private void onNew() {
		Long type = getIdTypeForNew();

		if (null != type) {
			AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
			ape.setMessage(MessageConstants.Message.D_LIST_VIEW_NEW);
			ape.setEntityId(type);
			controller.handleAppEvent(ape);
		}
		//else do nothing
	} //END private void onNew()

	/**
	 * Shows an edit view for en entry.
	 */
	private void onEdit() {
		Long rowID = getSelectedRowID();
		if (null != rowID) {
			AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
			ape.setMessage(msgEdit);
			ape.setEntityId(rowID);
			controller.handleAppEvent(ape);
		}
	} //END private void onEdit()

	private void onDelete() {
		Long rowID = getSelectedRowID();
		if (rowID != null) {
			AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
			ape.setMessage(MessageConstants.Message.D_LIST_VIEW_DELETE);
			ape.setEntityId(rowID);
			controller.handleAppEvent(ape);
		}
	} //END private void onDelete()

	private void onTableSelection(ListSelectionEvent evt) {
		ListSelectionModel lsm = (ListSelectionModel)evt.getSource();
		if (lsm.isSelectionEmpty()) {
			editB.setEnabled(false);
			deleteB.setEnabled(false);
		}
		else {
			editB.setEnabled(true);
			deleteB.setEnabled(true);
		}
	} //END private void onTableSelection(ListSelectionEvent)
} //END public class ListView extends JPanel implements IListView
