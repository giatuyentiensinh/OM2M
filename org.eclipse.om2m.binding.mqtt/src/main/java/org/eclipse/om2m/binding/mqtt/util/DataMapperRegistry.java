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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.datamapping.service.DataMapperService;

/**
 * This class is used to store instances of {@link DataMapperService} classes.
 *
 */
public class DataMapperRegistry {

	/** Private constructor to avoid creation */
	private DataMapperRegistry(){}
	
	/**
	 * Service registry classified by data type handled.
	 */
	private static Map<String, DataMapperService> serviceRegistery = new HashMap<String, DataMapperService>();

	/**
	 * Add a new {@link DataMapperService} to the registery.
	 * @param dms the service to register
	 */
	public static void register(DataMapperService dms){
		if(dms != null && dms.getServiceDataType() != null){
			serviceRegistery.put(dms.getServiceDataType(), dms);
		}
	}

	/**
	 * Retrieve a {@link DataMapperService} from a data type.
	 * @param dataType the 
	 * @return the {@link DataMapperService} that handle the data type or null if none
	 */
	public static DataMapperService get(String dataType){
		return serviceRegistery.get(dataType);
	}
	
	/**
	 * Remove the {@link DataMapperService} from the registry
	 * @param dataType the data type of the service to remove
	 */
	public static void remove(String dataType){
		serviceRegistery.remove(dataType);
	}
	
	/**
	 * Remove the {@link DataMapperService} from the registry
	 * @param dms the service to remove from the registry
	 */
	public static void remove(DataMapperService dms){
		remove(dms.getServiceDataType());
	}
	
	/**
	 * Retrieve the {@link DataMapperService} from the registry 
	 * from the MQTT format String
	 * @param format the format of the DMS
	 * @return the DMS with the specified format or null
	 */
	public static DataMapperService getFromMqttFormat(String format){
		switch (format) {
		case "xml":
			return DataMapperRegistry.get(MimeMediaType.XML);
		case "json":
			return DataMapperRegistry.get(MimeMediaType.JSON);
		default:
			return null;
		}
	}
	
}
