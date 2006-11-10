/*
 * AIdItemModel.java
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

import java.util.Date;

import net.vanosten.dings.consts.Constants;

public abstract class AIdItemModel extends AItemModel {

	/** The id of this model. */
	protected Long id;

	/** The date of the last update */
	protected Date lastUpd;

	/**
	 * Sets the last update date by converting a String representation of the date.
	 * If something goes wrong, then the current date is used.
	 *
	 * @param String aDateString - the last updated date as a String (YYYYMMDD HH24:MI:SS)
	 */
	protected final void setLastUpd(String aDateString) {
		lastUpd = Constants.getDateFromString(aDateString, new Date(), logger);
	} //END protected final void setLastUpd(String)

	/**
	 * Formats the last update date as a String with the format
	 * YYYYMMDD HH24:MI:SS.
	 * @return String date - the date of last update as a formatted String
	 */
	protected final String getLastUpdString() {
		return Constants.getDateString(lastUpd);
	} //END protected final String getLastUpdString()

	/**
	 * Returns the model's id.
	 *
	 * @return the id
	 */
	public final Long getId() {
		return id;
	} //END public long getId()

	/**
	 * Overrides <code>AItemModel.sendSaveNeeded()</code> to set the last update date.
	 */
	protected void sendSaveNeeded() {
		lastUpd = new Date();
		super.sendSaveNeeded();
	} //END protected void sendSaveNeeded()
	/**
	 * Returns an Object array with status, level, origin and destination information for display
	 * in EntriesListView..
	 *
	 * @return Object[] keyInfo
	 */
	protected abstract Object[] getTableDisplay();
} //END public abstract class AIdItemModel extends AItemModel
