/*
 * Toolbox.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 2005 Rick Gruber-Riemer (rick@vanosten.net)
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

import java.util.Locale;
import java.util.ResourceBundle;

import net.vanosten.dings.consts.Constants;

/**
 * Singleton to handle singleton-like access to unique classes in this application.
 * See http://www-106.ibm.com/developerworks/webservices/library/co-single.html for a discussion.
 * http://www.onjava.com/pub/a/onjava/2003/08/27/singleton.html?page=1 shows how Tomcat uses singletons
 * to manage ResourceBundles.
 * See also Effective Java: Programming Language Guide: Item 2: Enforce the singleton property with a private constructor
 * 
 * Right now the class only organizes the access to ResourceBundles and Preferences and the Locale
 */
public class Toolbox {
	
	/** The Singleton instance of this class */
	private final static Toolbox INSTANCE = new Toolbox();
	
	/** The preferences of this application */
	private final Preferences preferences;
	
	/** One Resourcebundel for all */
	private ResourceBundle bundle;
	
	/** The current locale */
	private Locale currentLocale;

	/**
	 * Private constructor to prevent instantiation from outside
	 */
	private Toolbox() {
		currentLocale = Locale.ENGLISH;
		preferences = new Preferences();
		if (preferences.containsKey(Preferences.PROP_LOCALE)) {
			currentLocale = Constants.parseLocale(preferences.getProperty(Preferences.PROP_LOCALE));
		} else {
			//take the users desktop settings
			currentLocale = Locale.getDefault();
			preferences.setProperty(Preferences.PROP_LOCALE, currentLocale.toString());
		}
		Locale.setDefault(currentLocale);
		bundle = ResourceBundle.getBundle("labels",currentLocale);
	} //END private Toolbox()
	
	/**
	 * Returns a singleton instance
	 * @return
	 */
	public static final Toolbox getInstance() {
		return INSTANCE;
	} //END public static final Toolbox getInstance()
	
	/**
	 * Gives access to the (singleton) instance of the Preferences
	 * @return a pointer to the Preferences of this application
	 */
	public final Preferences getPreferencesPointer() {
		return preferences;
	} //END public final Preferences getPreferencesPointer()
	
	/**
	 * Gives access to the (singleton) instance of the Locale object
	 * @return
	 */
	public final Locale getCurrentLocalePointer() {
		return currentLocale;
	} //END public final Locale getCurrentLocalePointer()
	
	/**
	 * This method is a wrapper for access to externalized strings.
	 * Theoretically several Resourcebundels could be supported by parsing the key
	 * and working with namespaces, where the first namespace would be the bundle.
	 * E.g. labels.menu.file and messages.help.about would be processed by 
	 * a ResourceBundle "labels" respectively "messages".
	 * This makes it unecessary to provide a pointer to the or a ResourcBundle instance.
	 * 
	 * @param key the key of the String to loclize as specified in the properties
	 * @return a localized String for the current Locale if specified
	 */
	public final String getLocalizedString(String key) {
		return bundle.getString(key);
	} //END public final String getLocalizedString(String)
} //END public class Toolbox
