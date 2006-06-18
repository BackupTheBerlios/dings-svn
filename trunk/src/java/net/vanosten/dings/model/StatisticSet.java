/*
 * StatisticSet.java
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

	public int getTotalNumberofEntries() {
		int totalNumberOfEntries = 0;
		for (int i = 0; i < Entry.SCORE_MAX; i++) {
			totalNumberOfEntries += numberOfEntries[i];
		}
		return totalNumberOfEntries;
	} //END public int getTotalNumberofEntries()

	public Date getTimeStamp() {
		return timeStamp;
	} //END public Date getTimeStamp()

	/**
	 * Calculates the arithmetic average score of the entries in this set
	 * @return
	 */
	public double getAverageScore() {
		int totalNumberOfEntries = 0;
		int totalScore = 0;
		for (int i = 0; i < Entry.SCORE_MAX; i++) {
			totalNumberOfEntries += numberOfEntries[i];
			totalScore += (i + 1) * numberOfEntries[i];
		}
		return (double) totalScore / totalNumberOfEntries;
	} //END public double getAverageScore()
} //END public class StatisticSet
