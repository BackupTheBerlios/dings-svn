/*
 * StatsCollection.java
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

import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;

import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.uiif.ISummaryView;

import org.jfree.data.DefaultCategoryDataset;

import java.util.logging.Logger;
import java.util.logging.Level;

public class StatsCollection implements IAppEventHandler {
	
	private ISummaryView statsView;
	
	/** Use a sorted Map with Date as key for the StatisticSets */
	private TreeMap items = new TreeMap();
	
	/** A pointer to entries collection */
	private EntriesCollection entries;
	
	/** A pointer to units collection */
	private UnitsCollection units;
	
	/** A pointer to categories collection */
	private CategoriesCollection categories;
	
	/** A pointer to EntryTypes collection */
	private EntryTypesCollection entryTypes;
	
	public final static int UNIT = 1;
	public final static int CATEGORY = 2;
	public final static int ENTRYTYPE = 3;
	public final static int SCORE = 4;

    private final IAppEventHandler parentController;
    
	private static Logger logger = Logger.getLogger("net.vanosten.dings.model.StatsCollection");

	public StatsCollection(IAppEventHandler anEventHandler) {
        this.parentController = anEventHandler;
	} //END public StatsCollection(IAppEventHandler)
	
	protected final void setItems(Map theItems) {
		items.clear();
		items.putAll(theItems);
	} //END protected final void setItems(Map)
	
	protected final void addItem(StatisticSet aSet) {
		items.put(aSet.getTimeStamp(), aSet);
	} //END protected final void addItem(StatisticSet)
	
	protected void setStatsView(ISummaryView aStatsView) {
		this.statsView = aStatsView;
	} //END protected void setStatsView(ISummaryView)
	
    /**
     * The collection as an XML string.
     */
	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();

		xml.append("<").append(Constants.XML_STATS).append(">").append(Constants.getLineSeparator());
		
		//run through collection
		Set keys = items.keySet();
		Iterator iter = keys.iterator();
		StatisticSet item;
		while (iter.hasNext()) {
			item = (StatisticSet)items.get(iter.next());
			xml.append(item.getXMLString()).append(Constants.getLineSeparator());
		}
		
		xml.append("</").append(Constants.XML_STATS).append(">");
		return xml.toString();
	} //END protected String getXMLString()
	
	//implements IAppEventHandler
	public final void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent()", evt.getMessage() + "; " + evt.getDetails());
		}
		if (evt.isDataEvent()) {
			if (evt.getMessage().equals(MessageConstants.D_SUMMARY_VIEW_DISPLAY_UNITS)) {
				boolean onlyChosen = Boolean.getBoolean(evt.getDetails());
				this.makeEntriesPerUnitChart(UNIT, onlyChosen, units.getChoiceProxy()
						,"Entries per Unit", "Unit", "Number of Entries", "Entries per Unit");
			}
			if (evt.getMessage().equals(MessageConstants.D_SUMMARY_VIEW_DISPLAY_CATEGORIES)) {
				boolean onlyChosen = Boolean.getBoolean(evt.getDetails());
				this.makeEntriesPerUnitChart(CATEGORY, onlyChosen, categories.getChoiceProxy()
						,"Entries per Category", "Category", "Number of Entries", "Entries per Categories");
			}
			if (evt.getMessage().equals(MessageConstants.D_SUMMARY_VIEW_DISPLAY_ENTRYTYPES)) {
				boolean onlyChosen = Boolean.getBoolean(evt.getDetails());
				this.makeEntriesPerUnitChart(ENTRYTYPE, onlyChosen, entryTypes.getChoiceProxy()
						,"Entries per Entry Type", "Entry Type", "Number of Entries", "Entries per Entry Type");
			}
			if (evt.getMessage().equals(MessageConstants.D_SUMMARY_VIEW_DISPLAY_SCORES)) {
				String[][] 	idAndName = {{"1", "1"},{"2", "2"},{"3", "3"},{"4", "4"},{"5", "5"},{"6", "6"},{"7", "7"}};
				boolean onlyChosen = Boolean.getBoolean(evt.getDetails());
				this.makeEntriesPerUnitChart(SCORE, onlyChosen, idAndName
						,"Entries per Score", "Score", "Number of Entries", "Entries per Score");
			}
		}
	} //END public final void handleAppEvent(AppEvent)
	
	/**
	 * Adds the current learning statistics as a new StatisticSet
	 */
	protected Date addNewStatisticSet() {
		//make new StatisticSet
		Date now = new Date();
		StatisticSet set = new StatisticSet(now, entries.getEntriesScoreStats());
		//add to collection
		this.addItem(set);
		
		//send save needed to the application
		AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
		ape.setMessage(MessageConstants.S_SAVE_NEEDED);
		parentController.handleAppEvent(ape);
		return now;
	} //END protected final void addNewStatisticSet()
	
	/**
	 * @param categories The categories to set.
	 */
	protected void setCategories(CategoriesCollection categories) {
		this.categories = categories;
	} //END protected void setCategories(CategoriesCollection)
	
	/**
	 * @param entries The entries to set.
	 */
	protected void setEntries(EntriesCollection entries) {
		this.entries = entries;
	} //END protected void setEntries(EntriesCollection)
	
	/**
	 * @param entryTypes The entryTypes to set.
	 */
	protected void setEntryTypes(EntryTypesCollection entryTypes) {
		this.entryTypes = entryTypes;
	} //END protected void setEntryTypes(EntryTypesCollection)
	
	/**
	 * @param units The units to set.
	 */
	protected void setUnits(UnitsCollection units) {
		this.units = units;
	} //END protected void setUnits(UnitsCollection)
	
	private final void makeEntriesPerUnitChart(int type, boolean onlyChosenEntries, String[][] idAndName
			, final String title, final String categoryTitle, final String valueTitle, final String serie) {
		//get the id and display name

		final String[] theIds = new String[idAndName.length];
		for (int i = 0; i < theIds.length; i++) {
			theIds[i] = idAndName[i][0];
		}
		final int[] theStats = entries.getEntriesPerXStats(onlyChosenEntries, theIds, type);
		
		//Create the dataset
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < theIds.length; i++) {
        	dataset.addValue(new Integer(theStats[i]), serie, idAndName[i][1]);
        }
        //tell GUI to display a barchart
        statsView.displayHorizontalBarChart(dataset, title, categoryTitle, valueTitle);
	} //END private final void makeEntriesPerUnitChart(...)
} //END public final class StatsCollection implements IAppEventHandler
