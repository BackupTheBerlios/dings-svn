/*
 * IDTableModel.java
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
package net.vanosten.dings.swing.helperui;

import javax.swing.table.AbstractTableModel;

/**
 * IDTableModel is an inplementation of AbstractTableModel
 * that holds the ID of a record in the first column,
 * which is never shown.
 * 
 * @author Rick Gruber
 */
public class IDTableModel extends AbstractTableModel {
	
	private String[] columnNames;
	protected Object[][] data;
	
	public IDTableModel(String[] theColumnNames, Object[][] theData) {
		this.columnNames = theColumnNames;
		this.data = theData;
	} //END public IDTableModel(String[], Object[][])
	
	//Implements AbstractTableModel
	public Object getValueAt(int row, int col) {
		return data[row][col + 1];
	} //END public Object getValueAt(int, int)
	
	//Implements AbstractTableModel
	public int getRowCount() {
		return data.length;
	} //END public int getRowCount()
	
	//Implements AbstractTableModel.
	public int getColumnCount() {
		return columnNames.length;
	} //END public int getColumnCount()

	/**
	 * Overrides the default method in AbstractTableModel
	 * by providing the given column nmae as given in the
	 * constructor.
	 * 
	 * @param int col - the column number.
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	} //END public String getColumnName(int)	
	
	/**
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	} //END public Class getColumnClass(int)

	/**
	 * @param int pos - the position in the table
	 * @return String - the ID at the position or null if position not found
	 */
	public String getIDAt(int pos) {
		if ((0 > pos) ||(data.length <= pos)) {
			return null;
		}
		return (String)data[pos][0];
	} //END public String getIDAt(int)
	
	/**
	 * Returns the index pos of an ID or -1 if ID cannot be found
	 */
	public int getIDIndexPos(String anID) {
		for (int i = 0; i < data.length; i++) {
			if (anID.equals(data[i][0])) {
				return i;
			} 
		}		
		return -1;
	} //END public int getIDIndexPos(String)
	
	public Object[][] getData() {
		Object[][] copiedData = new Object[data.length][];
		System.arraycopy(data, 0, copiedData, 0, data.length);
		return copiedData;
	} //END public Object[][] getData()
} //END public class IDTableModel extends AbstractTableModel
