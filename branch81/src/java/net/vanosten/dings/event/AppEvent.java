/*
 * AppEvent.java
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
 package net.vanosten.dings.event;

import java.util.EventObject;

import net.vanosten.dings.consts.MessageConstants.Message;

/**
 * This class represents an ApplicationEvent.
 * The original class was published in a www.javaworld.com article:
 * HMVC: The layered pattern for developing strong client tiers.
 */
public class AppEvent extends EventObject {
	private final static long serialVersionUID = 1L;

	public enum EventType {NAV_EVENT
		, STATUS_EVENT
		, DATA_EVENT
		, HELP_EVENT
	}

	/** The message header */
	private Message message;

	/** The details of the message */
	private String details;

	/** The type of event */
	private EventType eventType;

	public AppEvent(EventType anEventType) {
		super(anEventType);
		eventType = anEventType;
	} //END public AppEvent(String)

	public boolean isNavEvent() {
		if(EventType.NAV_EVENT == eventType) {
			return true;
		}
		return false;
	} //END public boolean isNavEvent()

	public boolean isStatusEvent() {
		if(EventType.STATUS_EVENT == eventType) {
			return true;
		}
		return false;
	} //END public boolean isStatusEvent()

	public boolean isDataEvent() {
		if(EventType.DATA_EVENT == eventType) {
			return true;
		}
		return false;
	} //END public boolean isDataEvent()

	public boolean isHelpEvent() {
		if(EventType.HELP_EVENT == eventType) {
			return true;
		}
		return false;
	} //END public boolean isHelpEvent()

	public void setMessage(Message aMessage) {
		message = aMessage;
	} //END public void setMessage(Message)

	public Message getMessage() {
		return message;
	} //END public Message getMessage()

	public void setDetails(String aDetails) {
		details = aDetails;
	} //END public void setDetails(String)

	public String getDetails() {
		return details;
	} //END public String getDetails()
} //END public class AppEvent extends EventObject
