/*
 * AModel.java
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

import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.event.AppEvent;

import java.util.logging.Logger; 

public abstract class AModel implements IAppEventHandler {

	/** The controller */
	protected IAppEventHandler parentController;

	/** The log4j logger */
	protected static Logger logger;

	/**
	 * Sets the controller.
	 * @param aController
	 */
	public void setParentController(IAppEventHandler aController) {
		this.parentController = aController;
	} //END public void setParentController(IAppEventHandler)

	/**
	 * Releases the views to make them available for GC.
	 */
	protected abstract void releaseViews();

	/**
	 * Updates the model with data from the GUI
	 */
	protected abstract void updateModel();

	/**
	 * Updates the GUI with the values in the model
	 */
	protected abstract void updateGUI();

	/**
	 * Handle application events
	 *
	 * @param AppEvent evt
	 */
	public void handleAppEvent(AppEvent evt) {
	} //END public void handleAppEvent(AppEvent)
} //END public abstract class AModel implements IAppEventHandler
