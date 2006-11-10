/*
 * IEntriesSelectionView.java
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
package net.vanosten.dings.uiif;

public interface IEntriesSelectionView extends IView {
	public void setUnitsList(String[][] theUnits);
	public void setCategoriesList(String[][] theCategories);
	public void setTypesList(String[][] theEntryTypes);
	public int getStatusChoice();
	public void setStatusChoice(int aStatusChoice);
	public Long[] getUnitsChoice();
	public void setUnitsChoice(Long[] theIds);
	public Long[] getCategoriesChoice();
	public void setCategoriesChoice(Long[] theIds);
	public Long[] getTypesChoice();
	public void setTypesChoice(Long[] theIds);
	public String getLastLearnedBefore();
	public void setLastLearnedBefore(int aNumberOfDays);
	public int[] getMinMaxScore();
	public void setMinMaxScore(int[] theLevels);
} //END public interface IEntriesSelectionView extends IView