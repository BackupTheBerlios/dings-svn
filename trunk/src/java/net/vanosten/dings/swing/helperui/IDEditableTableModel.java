/*
 * IDTableModel.java
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
package net.vanosten.dings.swing.helperui;

/**
 * This model extends <code>IDTableModel</code> and allows the follwing functionality:
 * <ul>
 * <li>Change the value of designated cells</li>
 * <li>Add new rows</li>
 * <li>Delete rows</li>
 * <li>Move rows up</li>
 * <li>Move rows down</li>
 * </ul>
 * @author Rick.Gruber
 */
public class IDEditableTableModel extends IDTableModel {
	private final static long serialVersionUID = 1L;
		
	/** Determines whether a column is editable or not.
	 * The first row should always be non-editable, as it is the ID
	 */
	private boolean[] columnEditable = null;
	
	public IDEditableTableModel(String[] theColumnNames, Object[][] theData, boolean[] theEditableColumns) {
		super(theColumnNames, theData);
		columnEditable = theEditableColumns;
	} //END public IDEditableTableModel(String[], Object[][], boolean[])
	
	/**
	 * Tells the renderer in JTable whether or not the current
	 * cell is editable. The implementation is only based on
	 * columns, not on rows.
	 */
	public boolean isCellEditable(int row, int col) {
		if (null == columnEditable) {
			//the values have not been set so just return false
			return false;
		}
		return columnEditable[col+1];
	} //END public boolean isCellEditable(int, int)
	
	/**
	 * Allows the JTable to set a value in the model.
	 * Actually everybody can use this method to change the content.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col+1] = value;
		fireTableDataChanged();
	} //END public void setValueAt(...)
	
	/**
	 * Deletes a specific row.
	 * 
	 * @param int aRow - the position of the row to be deleted
	 */
	public void deleteRow(int aRow) {
		Object[][] theData = new Object[data.length-1][];
		for (int i = 0; i < aRow; i++) {
			theData[i] = data[i];
		}
		for (int i = aRow + 1; i < data.length; i++) {
			theData[i-1] = data[i];
		}
		data = theData;
		fireTableDataChanged();
	} //END public void deleteRow()
	
	/**
	 * Adds a new row of data at the last position.
	 * 
	 * @param Object[] newData - the data to be inserted
	 */
	public void addRow(Object[] newData) {
		Object[][] theData = new Object[data.length+1][];
		for (int i = 0; i < data.length; i++) {
			theData[i] = data[i];
		}
		theData[data.length] = newData;
		data = theData;
		fireTableDataChanged();		
	} //END public void addRow(int, Object[])
	
	/**
	 * Moves a row up or down in the sequence.
	 * Upwards means towards a smaller index number, i.e.
	 * more to the top of the table.
	 * 
	 * @param int rowToMove - the position of the row to move
	 * @param boolean upWards - true if the row has to be moved upwards
	 */
	public void moveRow(int rowToMove, boolean upWards) {
		//moving a row upwards is the same as as moving the
		//row before downwards.
		int toGo = rowToMove;
		if (upWards) {
			toGo -= 1;
		}
		Object[][] theData = new Object[data.length][];
		for (int i = 0; i < toGo; i++) {
			theData[i] = data[i];
		}
		theData[toGo] = data[toGo + 1];
		theData[toGo + 1] = data[toGo];
		for (int i = toGo + 2; i < data.length; i++) {
			theData[i] = data[i];
		}
		data = theData;
		fireTableDataChanged();
	} //END public void moveRow(int, boolean)
} //END public class IDEditableTableModel extends IDTableModel
