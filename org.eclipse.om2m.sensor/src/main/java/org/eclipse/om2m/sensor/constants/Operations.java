/*******************************************************************************
 * Copyright (c) 2013-2016 LAAS-CNRS (www.laas.fr)
 * 7 Colonel Roche 31077 Toulouse - France
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial Contributors:
 *     Thierry Monteil : Project manager, technical co-manager
 *     Mahdi Ben Alaya : Technical co-manager
 *     Samir Medjiah : Technical co-manager
 *     Khalil Drira : Strategy expert
 *     Guillaume Garzone : Developer
 *     François Aïssaoui : Developer
 *
 * New contributors :
 *******************************************************************************/
package org.eclipse.om2m.sensor.constants;

import org.eclipse.om2m.commons.exceptions.BadRequestException;
/**
 * Represent a operation 
 *
 */
public enum Operations {
	
	GET_STATE("getState"),
	SET_ON_LEB_RED("setOnLebRed"),
	SET_ON_LEB_GREEN("setOnLebGreen"),
	SET_ON_LEB_BLUE("setOnLebBlue"),
	SET_OFF_LEB_RED("setOffLebRed"),
	SET_OFF_LEB_GREEN("setOffLebGreen"),
	SET_OFF_LEB_BLUE("setOffLebBlue"),
	GPIO("gpio"),
	OBSERVEBTNACTIVE("observeBtnActive"),
	OBSERVEBTNCANCEL("observeBtnCancel");
	private final String value;
	
	private Operations(final String value){
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
	
	public String getValue(){
		return value;
	}
	
	/**
	 * Return the operation from the string
	 * @param operation
	 * @return
	 */
	public static Operations getOperationFromString(String operation){
		for(Operations op : values()){
			if(op.getValue().equals(operation)){
				return op;
			}
		}
		throw new BadRequestException("Unknow Operation");
	}
}
