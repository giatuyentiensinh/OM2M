/*******************************************************************************
 *  Copyright (c) 2013-2016 LAAS-CNRS (www.laas.fr)
 *  7 Colonel Roche 31077 Toulouse - France
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Initial Contributors:
 *      Thierry Monteil : Project manager, technical co-manager
 *      Mahdi Ben Alaya : Technical co-manager
 *      Samir Medjiah : Technical co-manager
 *      Khalil Drira : Strategy expert
 *      Guillaume Garzone : Developer
 *      François Aïssaoui : Developer
 *
 *   New contributors :
 *******************************************************************************/
package org.eclipse.om2m.binding.mqtt.util;

import java.util.concurrent.Semaphore;

import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class ResponseSemaphore {

	private Semaphore semaphore;
	private ResponsePrimitive responsePrimitive;

	public ResponseSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public ResponsePrimitive getResponsePrimitive() {
		return responsePrimitive;
	}

	public void setResponsePrimitive(ResponsePrimitive responsePrimitive) {
		this.responsePrimitive = responsePrimitive;
	}

}
