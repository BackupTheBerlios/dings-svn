/*
 * StatisticSet.java
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

import java.util.Date;

import net.vanosten.dings.consts.Constants;

public final class StatisticSet {
	
	private final Date timeStamp;
	private final int[] numberOfEntries;
	
	public StatisticSet(Date aTimeStamp, int[] aNumberOfEntries) {
		this.timeStamp = aTimeStamp;
		this.numberOfEntries = aNumberOfEntries;
	} //END public StatisticSet(Date, int[])

	public String getXMLString() {
		StringBuffer xml = new StringBuffer();
		xml.append("<").append(Constants.XML_STATS_SET);
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_STATS_TIMESTAMP, Constants.getDateString(timeStamp)));
		xml.append(">");
		for (int i = 0; i < Entry.SCORE_MAX; i++) {
			xml.append("<").append(Constants.XML_STATS_NOF_ENTRIES);
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_SCORE, Integer.toString(i+1)));
			xml.append(">");
			xml.append(numberOfEntries[i]);
			xml.append("</").append(Constants.XML_STATS_NOF_ENTRIES).append(">");			
		}
		xml.append("</").append(Constants.XML_STATS_SET).append(">");
		return xml.toString();
	} //END public String getXMLString()
	
	protected int[] getNumberOfEntries() {
		return numberOfEntries;
	} //END protected int[] getNumberOfEntries()
	
	public Date getTimeStamp() {
		return timeStamp;
	} //END public Date getTimeStamp()
} //END public class StatisticSet
