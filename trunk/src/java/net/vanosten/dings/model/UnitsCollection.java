/*
 * UnitsCollection.java
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
package net.vanosten.dings.model;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.event.IAppEventHandler;

public class UnitsCollection extends AChoiceCollection {
    /** The id for the actual Unit */
    private Unit currentItem = null;

    public UnitsCollection(IAppEventHandler anEventHandler) {
        super(anEventHandler);
    }	//End public UnitsCollection(IAppEventHandler)
    
	//implements ACollection
	protected void setTagName() {
		tagName = Constants.XML_UNITS;
	} //END protected void setTagName()
    
	//implements ACollection
	protected void setMessageListView() {
		msgListView = MessageConstants.N_VIEW_UNITS_LIST;
	} //END protected void setMessageListView()
    
	//implements AChoiceCollection
	protected void setMessageEditView() {
		msgEditView = MessageConstants.N_VIEW_UNIT_EDIT;
	} //END protected void setMessageEditView()
	
    //implements ACollection
	protected void setItems(HashMap theUnits) {
        this.items = theUnits;

        Set allKeys = items.keySet();
        Iterator iter = allKeys.iterator();
        if (iter.hasNext()) setCurrentItem((String)iter.next());
    } //End protected void setItems(HashMap)

	protected Unit getCurrentItem() {
		return currentItem;
	} //END protected Unit getCurrentItem()

    //implements ACollection
    protected void setCurrentItem(String anID) {
    	if (null != currentItem) {
    		//release the views of the current
    		currentItem.releaseViews();
    	}
    	if (null == anID) {
    		currentItem = null;
    	}
        else if (items.containsKey(anID)) {
            currentItem = (Unit)items.get(anID);
            currentItem.setParentController(this);
        }
    }	//END private void setCurrentItem(String)
    
    //implements AChoiceCollection
    protected void deleteItem() {
    	deleteItem(currentItem.getId(), false);
    } //END protected void deleteItem()

	//implements ACollection
	//the type attribute is not used for Units
    protected void newItem(String aType, boolean isDefault) {
        Unit newUnit = null;
        newUnit = Unit.newItem(isDefault);
        items.put(newUnit.getId(), newUnit);
        setCurrentItem(newUnit.getId());
        //save needed
        sendSaveNeeded();
    }   //End public void newItem(String)

    //implements ACollection
    protected void refreshListView() {
		Object[][] data = new Object[items.size()][AUnitCategory.getTableDisplayTitles().length]; 

		Unit item;
		Set keys = items.keySet();
		Iterator iter = keys.iterator();
		for (int i = 0; i < data.length; i++) {
			item = (Unit)items.get(iter.next());
			data[i] = item.getTableDisplay();
		}
		if (listView != null) {
			listView.setList(AUnitCategory.getTableDisplayTitles(), data, AUnitCategory.getTableColumnFixedWidth());
		}
        if (null != currentItem) listView.setSelected(currentItem.getId());
    }	//End private void resetListView()

	//implements AChoiceCollection
    public String[][] getChoiceProxy() {
        String theIDsAndNames[][] = new String[items.size()][2];
        Set keys = items.keySet();
        Unit item;
        Iterator iter = keys.iterator();
        for (int i = 0; i < theIDsAndNames.length; i++) {
            item = (Unit)items.get(iter.next());
            theIDsAndNames[i] = item.getChoiceProxy();
        }
        return theIDsAndNames;
    }	//END public String[][] getChoiceProxy()
}	//End public class UnitsCollection

