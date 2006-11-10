/*
 * Category.java
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
package net.vanosten.dings.model;

import java.awt.Color;
import java.util.logging.Logger;

import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.utils.Toolbox;

public final class Category extends AUnitCategory {

	public Category(long anId, String aLastUpd, String aName, String aDescription, Color aColor) {
		super(anId, aLastUpd, aName, aDescription, aColor);
		logger = Logger.getLogger("net.vanosten.dings.model.Category");
	} //END public Category(...)

	/**
	 * Construct a new Category from scratch adding the necessary information.
	 */
	protected static Category newItem(boolean isDefault) {
		if (isDefault) {
			return new Category(Toolbox.getInstance().nextId(), null, "Default", Constants.EMPTY_STRING, null);
		}
		return new Category(Toolbox.getInstance().nextId(), null, Constants.UNDEFINED, Constants.EMPTY_STRING, null);
	} //END protected static Category newItem(boolean)

	//Implements AItemModel
	protected String getXMLString() {
		return super.constructXMLString(Constants.XML_CATEGORY);
	} //END protected String getXMLString()
} //END public class Category extends AUnitCategory

