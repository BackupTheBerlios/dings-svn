/*
 * AppEvent.java
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
 package net.vanosten.dings.event;

import java.util.EventObject;

/**
 * This class represents an ApplicationEvent.
 * The original class was published in a www.javaworld.com article:
 * HMVC: The layered pattern for developing strong client tiers.
 */ 
public class AppEvent extends EventObject {
    public final static String NAV_EVENT = "NAV_EVENT";
    public final static String STATUS_EVENT = "STATUS_EVENT";
    public final static String DATA_EVENT = "DATA_EVENT";
	public final static String HELP_EVENT = "HELP_EVENT";
    
    /** The message header */
	private String message;
	
	/** The details of the message */
	private String details;
	
	/** The type of event */
    private String eventType;
    
    public AppEvent(String anEventType) {
        super(anEventType);
        eventType = anEventType;
    }	//END public AppEvent(String)
    
    public boolean isNavEvent() {
        if(eventType.equals(NAV_EVENT))
            return true;
        else
            return false;
    }	//END public boolean isNavEvent()
    
    public boolean isStatusEvent() {
        if(eventType.equals(STATUS_EVENT))
            return true;
        else
            return false;
    }	//END public boolean isStatusEvent()

    public boolean isDataEvent() {
        if(eventType.equals(DATA_EVENT))
            return true;
        else
            return false;
    }	//END public boolean isDataEvent()

    public boolean isHelpEvent() {
        if(eventType.equals(HELP_EVENT))
            return true;
        else
            return false;
    }	//END public boolean isHelpEvent()

    public void setMessage(String aMessage) {
        message = aMessage;
    }	//END public void setMessage(String)
    
    public String getMessage() {
        return message;
    }	//END public String getMessage()

    public void setDetails(String aDetails) {
        details = aDetails;
    }	//END public void setDetails(String)
    
    public String getDetails() {
        return details;
    }	//END public String getDetails()
}	//End public class AppEvent extends EventObject
