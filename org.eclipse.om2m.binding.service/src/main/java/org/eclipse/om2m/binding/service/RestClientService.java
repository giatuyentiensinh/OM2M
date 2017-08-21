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
package org.eclipse.om2m.binding.service;

import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

/**
 * Rest client interface.
 */
public interface RestClientService {
	/**
	 * Sends a {@link RequestIndication} and receives a {@link ResponseConfirm}.
	 * @param requestIndication - The generic request to handle.
	 * @return ResponseConfirm - The generic returned response.
	 */
	public ResponsePrimitive sendRequest(RequestPrimitive requestIndication);

	/**
	 * Returns the communication protocol name
	 * @return String
	 */
	public String getProtocol();

}
