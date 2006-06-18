/*
 * IEntryEditView.java
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

public interface IEntryEditView extends IDetailsView {
	public String getBase();
	public void setBase(String anBase);
	public String getTarget();
	public void setTarget(String aTarget);
	public void setEntryType(String aLabel, String anId);
	public void setEntryTypes(String[][] theEntryTypes);
	public String getAttributeId(int aNumber);
	public void setAttributeId(String anAttributeId, int aNumber);
	public void setAttributeName(String anAttributeName, int aNumber);
	public void setAttributeItems(String[][] theItems, int aNumber);
	public String getExplanation();
	public void setExplanation(String anExplanation);
	public String getExample();
	public void setExample(String anExample);
	public String getPronunciation();
	public void setPronunciation(String aPronunciation);
	public String getRelation();
	public void setRelation(String aRelation);
	public String getUnitId();
	public void setUnitId(String aUnitId);
	public void setUnits(String theUnits[][]);
	public String getCategoryId();
	public void setCategoryId(String aCategoryId);
	public void setCategories(String theCategories[][]);
	public boolean getStatus();
	public void setStatus(boolean aStatus);
	public void setBaseIsValueValid(boolean valid);
	public void setTargetIsValueValid(boolean valid);
} //END public interface IEntryEditView extends IDetailsView
