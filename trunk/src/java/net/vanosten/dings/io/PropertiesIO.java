/*
 * PropertiesIO.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
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
package net.vanosten.dings.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesIO {

	/** The name of the application property file */
	private final static String PROP_FILENAME = ".dings.properties";

	/** The comment in the properties file */
	private final static String PROP_COMMENT = "Properties for DingsBums?!";

	/** The log4j logger */
	protected static Logger logger = Logger.getLogger(PropertiesIO.class.getName());;
	
	/**
	 * Private constructor because only static methods
	 */
	private PropertiesIO() {
		//nothing to do
	}

	/**
	 * Reads the properties from a file in the users directory
	 * 
	 * @return a set of properties read form a Java properties file. If the file cannot be read
	 *         then an empty Properties object is returned.
	 */
	public static Properties readFromFile() {
		Properties props = new Properties();
		FileInputStream in = null;
		try {
			String userHome = System.getProperty("user.home");
			in = new FileInputStream(userHome + File.separator + PROP_FILENAME);
			props.load(in);
		}
		catch (Exception e) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, PropertiesIO.class.getName(), "readFromFile()", e.getMessage());
			}
			//do nothing
		}
		finally {
			if (null != in) {
				try {
					in.close();
				}
				catch (IOException e) {
					if (logger.isLoggable(Level.FINEST)) {
						logger.logp(Level.FINEST, PropertiesIO.class.getName(), "readFromFile()", e.getMessage());
					}
					//do nothing

				}
			}
			in = null;
		}
		return props;
	}

	/**
	 * Saves the properties to a file in the users directory.
	 * 
	 * @param props - The Properties to write to the file
	 */
	public static void saveToFile(Properties props) {
		FileOutputStream out = null;
		try {
			String userHome = System.getProperty("user.home");
			out = new FileOutputStream(userHome + File.separator + PROP_FILENAME);
			props.store(out, PROP_COMMENT);
			out.close();
		}
		catch (Exception e) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, PropertiesIO.class.getName(), "saveToFile()", e.getMessage());
			}
			//do nothing
		}
		finally {
			if (null != out) {
				try {
					out.close();
				}
				catch (IOException e) {
					if (logger.isLoggable(Level.FINEST)) {
						logger.logp(Level.FINEST, PropertiesIO.class.getName(), "saveToFile()", e.getMessage());
					}
					//do nothing
				}
			}
		}
	}
}
