/*
 * IEntriesSelectionView.java
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
package net.vanosten.dings.uiif;

public interface IEntriesSelectionView extends IView {
	public void setUnitsList(String[][] theUnits);
	public void setCategoriesList(String[][] theCategories);
	public void setTypesList(String[][] theEntryTypes);
	public int getStatusChoice();
	public void setStatusChoice(int aStatusChoice);
	public String[] getUnitsChoice();
	public void setUnitsChoice(String[] theIds);
	public String[] getCategoriesChoice();
	public void setCategoriesChoice(String[] theIds);
	public String[] getTypesChoice();
	public void setTypesChoice(String[] theIds);
	public String getLastLearnedBefore();
	public void setLastLearnedBefore(int aNumberOfDays);
	public int[] getMinMaxScore();
	public void setMinMaxScore(int[] theLevels);
	public void setUnitsLabel(String aUnitsLabel);
	public void setCategoriesLabel(String aCategoriesLabel);
} //END public interface IEntriesSelectionView extends IView