/*
 * EntryTypeAttributeEditView.java
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
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.uiif.IEntryTypeAttributeEditView;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.swing.helperui.IDEditableTableModel;
import net.vanosten.dings.swing.helperui.ValidStringTableCellRenderer;
import net.vanosten.dings.swing.helperui.ValidatedTextField;
import net.vanosten.dings.model.EntryTypeAttributeItem;
import net.vanosten.dings.utils.Toolbox;

public class EntryTypeAttributeEditView extends AEditView implements IEntryTypeAttributeEditView, TableModelListener {
	private final static long serialVersionUID = 1L;

	private ValidatedTextField nameVTF;
	private ChoiceID defaultItemCh;
	private JPanel itemsP;
	private JPanel itemsTableButtonP;
	private JButton newItemB;
	private JButton deleteItemB;
	private JButton moveItemUpB;
	private JButton moveItemDownB;
	private JTable itemsTable;
	private IDEditableTableModel itemsModel;
	private ValidStringTableCellRenderer validStringRenderer;

	/** If the table with items has changes, then this is true */
	private boolean itemTableEdited = false;

	public EntryTypeAttributeEditView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.edit_entry_type_attribute")
				, aComponentOrientation
				, true
				, true
				, MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST);
	} //END public EntryTypeAttributeEditView(ComponentOrientation)

	//Implements AEditView
	protected void initializeEditP() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		editP.setLayout(gbl);

		//name
		JLabel nameL = new JLabel("Name:");
		nameL.setDisplayedMnemonic("N".charAt(0));
		nameVTF = new ValidatedTextField(50);
		nameVTF.setToolTipText("Name may not be empty");
		nameL.setLabelFor(nameVTF);
		nameVTF.addKeyListener(this);
		//default item
		JLabel defaultItemL = new JLabel("Default Item:");
		defaultItemL.setDisplayedMnemonic("FD".charAt(0));
		defaultItemCh = new ChoiceID();
		defaultItemL.setLabelFor(defaultItemCh);
		defaultItemCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onDefaultChange();
			}
		});
		//itemP
		initializeItemsP();

		//put together
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(nameL, gbc);
		editP.add(nameL);
		//------
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets (0,DingsSwingConstants.SP_H_G,0,0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(nameVTF, gbc);
		editP.add(nameVTF);
		//------
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.gridwidth = 1;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_C,0,0,0);
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(defaultItemL, gbc);
		editP.add(defaultItemL);
		//------
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_C,DingsSwingConstants.SP_H_G,0,0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(defaultItemCh, gbc);
		editP.add(defaultItemCh);
		//------
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G,0,0,0);
		gbc.weighty = 1.0d;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(itemsP, gbc);
		editP.add(itemsP);
		//set focus
		nameVTF.requestFocus();
	} //END protected void initializeEditP()

	private void initializeItemsP() {
		itemsP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		itemsP.setLayout(gbl);
		//populate panels
		initializeItemsTableButtonP();
		//special cell renderer
		validStringRenderer = new ValidStringTableCellRenderer();
		validStringRenderer.setToolTipText("The name must be unique and not be empty");
		itemsTable = new JTable();

		itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = itemsTable.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				//do check in special method
				onTableSelection(e);
			}
		});
		JScrollPane itemsPane = new JScrollPane(itemsTable);

		//put together
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0d;
		gbc.weighty = 1.0d;
		gbl.setConstraints(itemsPane, gbc);
		itemsP.add(itemsPane);
		//------
		gbc.gridx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0d;
		gbc.weighty = 0.0d;
		gbc.insets = new Insets (0,DingsSwingConstants.SP_H_G,0,0);
		gbl.setConstraints(itemsTableButtonP, gbc);
		itemsP.add(itemsTableButtonP);
	} //END private void initializeItemsP()

	private void initializeItemsTableButtonP() {
		itemsTableButtonP = new JPanel();
		itemsTableButtonP.setLayout(new GridLayout(4,1,0,DingsSwingConstants.SP_V_C));
		newItemB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.add_item")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_ADD_BTN, "FIXME"));
		newItemB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.add_item").charAt(0));
		newItemB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onNewItem();
			}
		});
		deleteItemB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.remove_item")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_REMOVE_BTN, "FIXME"));
		deleteItemB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.remove_item").charAt(0));
		deleteItemB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onDeleteItem();
			}
		});
		moveItemUpB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.move_up")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_UP_BTN, "FIXME"));
		moveItemUpB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.move_up").charAt(0));
		moveItemUpB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onMoveItem(true);
			}
		});
		moveItemDownB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.move_down")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_DOWN_BTN, "FIXME"));
		moveItemDownB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.move_down").charAt(0));
		moveItemDownB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onMoveItem(false);
			}
		});
		newItemB.setHorizontalAlignment(SwingConstants.LEADING);
		deleteItemB.setHorizontalAlignment(SwingConstants.LEADING);
		moveItemUpB.setHorizontalAlignment(SwingConstants.LEADING);
		moveItemDownB.setHorizontalAlignment(SwingConstants.LEADING);
		itemsTableButtonP.add(newItemB);
		itemsTableButtonP.add(deleteItemB);
		itemsTableButtonP.add(moveItemUpB);
		itemsTableButtonP.add(moveItemDownB);
	} //END private void initializeItemsTableButtonP()

	//implements IEntryTypeAttributeEditView
	public void setName(String aName) {
		nameVTF.setText(aName);
	} //END public void setUnitName(String)

	//implements IEntryTypeAttributeEditView
	public String getName() {
		return nameVTF.getText();
	} //END public String getName()

	//implements IEntryTypeAttributeEditView
	public void setDefaultItem(String anId) {
		isUpdating = true;
		defaultItemCh.setSelectedIndex(0);
		defaultItemCh.setSelectedID(anId);
		isUpdating = false;
	} //END public void setDefaultItem(String)

	//implements IEntryTypeAttributeEditView
	public String getDefaultItem() {
		return defaultItemCh.getSelectedID();
	} //END public String getDefaultItem()

	//implements IEntryTypeAttributeEditView
	public void setItems(Object[][] theItems) {
		//items table
		boolean[] editableColumns = new boolean[3];
		editableColumns[1] = true;
		editableColumns[2] = true;
		itemsModel = new IDEditableTableModel(EntryTypeAttributeItem.getTableDisplayTitles()
			, theItems
			, editableColumns);
		itemsTable.setModel(itemsModel);
		setSelectedItemRow(0);
		itemsModel.addTableModelListener(this);

		//editors and renderers (must be applied after valid model
		TableColumn namesColumn = itemsTable.getColumnModel().getColumn(0);
		namesColumn.setCellRenderer(validStringRenderer);

		//default item
		setDefaultItemItems();
	} //END public void setItems(Object[][])

	private void setDefaultItemItems() {
		//save the current selection
		String currentSelectedDefaultItemID = defaultItemCh.getSelectedID();
		//get data from table and transform
		Object[][] theItems = itemsModel.getData();
		String[][] listItems = new String[theItems.length][];
		for (int i = 0; i < listItems.length; i++) {
			listItems[i] = new String[2];
			listItems[i][0] = (String)theItems[i][0];
			listItems[i][1] = (String)theItems[i][1];
		}
		isUpdating = true;
		defaultItemCh.setItems(listItems);
		if (null != currentSelectedDefaultItemID) {
			defaultItemCh.setSelectedID(currentSelectedDefaultItemID);
		}
		isUpdating = false;
	} //END private void setDefaultItemItems(Object[][])

	//implements IEntryTypeAttributeEditView
	public Object[][] getItems() {
		return itemsModel.getData();
	} //END public Object[][] getItems()

	//implements IEntryTypeAttributeEditView
	public boolean isItemTableEdited() {
		return itemTableEdited;
	} //END public boolean isItemTableEdited()

	//implements IEntryTypeAttributeEditView
	public void setItemTableNotEdited() {
		itemTableEdited = false;
	} //END public void setItemTableNotEdited()

	//implements TableModelListener
	public void tableChanged(TableModelEvent evt) {
		//setEditing(true, true);
		itemTableEdited = true;
		setDefaultItemItems();
		onChange();
	} //END public void tableChanged(TableModelEvent)

	private void setSelectedItemRow(int pos) {
		itemsTable.setRowSelectionInterval(pos, pos);
		itemsTable.scrollRectToVisible(itemsTable.getCellRect(pos, 0, true));
	} //END public void setSelected(String)

	private void checkDeleteItemB() {
		int theSelectedRow = itemsTable.getSelectedRow();
		if (0 > theSelectedRow) {
			deleteItemB.setEnabled(false);
			return;
		}
		String anID = itemsModel.getIDAt(theSelectedRow);
		if (anID.equals(defaultItemCh.getSelectedID())) {
			deleteItemB.setEnabled(false);
		}
		else {
			deleteItemB.setEnabled(true);
		}
	} //END private void checkDeleteItemB()

	//-----------------------------------------------------------------------

	/**
	 * Checks whether the deleteItemB
	 */
	private void onDefaultChange() {
		checkDeleteItemB();

		//set changed
		onChange();
	} //END private void onDefaultChange()

	private void onNewItem() {
		Object[] newItem = (EntryTypeAttributeItem.newItem()).getTableDisplay();
		itemsModel.addRow(newItem);
		setSelectedItemRow(itemsModel.getRowCount() - 1);

		//set changed
		onChange();
	} //END private void onNewItem()

	private void onDeleteItem() {
		int lineToDelete = itemsTable.getSelectedRow();
		setSelectedItemRow(0); //this statement has to precede the next,
		//otherwise Nullpointer exception in checkDeletedItemB() -> anID
		itemsModel.deleteRow(lineToDelete);

		//set changed
		onChange();
	} //END private void onDeleteItem()

	private void onMoveItem(boolean upwards) {
		int newSelectionPos = itemsTable.getSelectedRow();
		itemsModel.moveRow(itemsTable.getSelectedRow(), upwards);
		if (upwards) {
			newSelectionPos--;
		}
		else {
			newSelectionPos++;
		}
		setSelectedItemRow(newSelectionPos);

		//set changed
		onChange();
	} //END private void onMoveItem(boolean)

	private void onTableSelection(ListSelectionEvent evt) {
		ListSelectionModel lsm = (ListSelectionModel)evt.getSource();
		if (lsm.isSelectionEmpty()) {
			deleteItemB.setEnabled(false);
			moveItemUpB.setEnabled(false);
			moveItemDownB.setEnabled(false);
		}
		else {
			//check deleteItemB
			checkDeleteItemB();
			//check moveItemDownB
			int selectedRow = lsm.getMinSelectionIndex(); //selection model is SINGLE
			if (0 == selectedRow) {
				moveItemUpB.setEnabled(false);
			}
			else {
				moveItemUpB.setEnabled(true);
			}
			//check moveItemUpB
			if ((itemsModel.getRowCount() - 1) == selectedRow) {
				moveItemDownB.setEnabled(false);
			}
			else {
				moveItemDownB.setEnabled(true);
			}
		}
	} //END private void onTableSelection(ListSelectionEvent)

	//implements IEntryTypeAttributeEditView
	public void setNameIsValueValid(boolean valid) {
		nameVTF.isValueValid(valid);
	} //END public void setNameIsValueValid(boolean)

	//implements IEntryTypeAttributeEditView
	public void setItemNameIsValueValid(String invalidId) {
		String invalid;
		if (null == invalidId) {
			invalid = null;
		} else {
			int pos = itemsModel.getIDIndexPos(invalidId);
			invalid = (String)itemsModel.getValueAt(pos, 0);
		}
		validStringRenderer.setInvalidString(invalid);
	} //END public void setItemNameIsValueValid(String

} //END public class EntryTpeAttributEditView extends AEditView implements IEntryTypeAttributeEditView
