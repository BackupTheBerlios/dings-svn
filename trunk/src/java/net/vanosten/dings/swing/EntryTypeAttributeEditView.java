/*
 * EntryTypeAttributeEditView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * Copyright (C) 2002, 2003 Rick Gruber (rick@vanosten.net)
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
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.uiif.IEntryTypeAttributeEditView;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.swing.helperui.IDEditableTableModel;
import net.vanosten.dings.model.EntryTypeAttributeItem;

public class EntryTypeAttributeEditView extends AEditView implements IEntryTypeAttributeEditView, TableModelListener {
	private JTextField nameTF;
	private ChoiceID defaultItemCh;
	private JPanel itemsP;
	private JPanel itemsTableButtonP;
	private JButton newItemB;
	private JButton deleteItemB;
	private JButton moveItemUpB;
	private JButton moveItemDownB;
	private JTable itemsTable;
	private IDEditableTableModel itemsModel;
		
	/** If the table with items has changes, then this is true */
	private boolean itemTableEdited = false;
	
	public EntryTypeAttributeEditView(ComponentOrientation aComponentOrientation) {
		super("Edit EntryTypeAttribute", aComponentOrientation, true, true, MessageConstants.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST);
	} //End public EntryTypeAttributeEditView(ComponentOrientation)

	//Implements AEditView
	protected void initializeEditP() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		editP.setLayout(gbl);

		//name
		JLabel nameL = new JLabel("Name:");
		nameL.setDisplayedMnemonic("N".charAt(0));
		nameTF = new JTextField(50);
		nameL.setLabelFor(nameTF);
		nameTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		//default item
		JLabel defaultItemL = new JLabel("Default Item:");
		defaultItemL.setDisplayedMnemonic("D".charAt(0));
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
		gbl.setConstraints(nameTF, gbc);
		editP.add(nameTF);
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
		nameTF.requestFocus();
	} //End protected void initializeEditP()
	
	private void initializeItemsP() {
		itemsP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		itemsP.setLayout(gbl);
		//populate panels
		initializeItemsTableButtonP();
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
		newItemB = new JButton("Add Item", Constants.createImageIcon(Constants.IMG_ADD_24, "FIXME"));
		newItemB.setMnemonic("A".charAt(0));
		newItemB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onNewItem();
			}
		});
		deleteItemB = new JButton("Remove Item", Constants.createImageIcon(Constants.IMG_REMOVE_24, "FIXME"));
		deleteItemB.setMnemonic("R".charAt(0));
		deleteItemB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onDeleteItem();
			}
		});
		moveItemUpB = new JButton("Move Up", Constants.createImageIcon(Constants.IMG_UP_24, "FIXME"));
		moveItemUpB.setMnemonic("U".charAt(0));
		moveItemUpB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onMoveItem(true);
			}
		});
		moveItemDownB = new JButton("Move Down", Constants.createImageIcon(Constants.IMG_DOWN_24, "FIXME"));
		moveItemDownB.setMnemonic("W".charAt(0));
		moveItemDownB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onMoveItem(false);
			}
		});
		itemsTableButtonP.add(newItemB);
		itemsTableButtonP.add(deleteItemB);
		itemsTableButtonP.add(moveItemUpB);
		itemsTableButtonP.add(moveItemDownB);
	}	//END private void initializeItemsTableButtonP()
	
	//implements IEntryTypeAttributeEditView
	public void setName(String aName) {
		nameTF.setText(aName);
	} //END public void setUnitName(String)
	
	//implements IEntryTypeAttributeEditView
	public String getName() {
		return nameTF.getText();
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
		setEditing(true);
		itemTableEdited = true;
		setDefaultItemItems();
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
}	//END public class EntryTpeAttributEditView extends AEditView implements IEntryTypeAttributeEditView
