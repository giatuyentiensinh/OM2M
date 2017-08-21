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

import java.util.regex.Pattern;

import org.eclipse.om2m.commons.constants.Constants;

/**
 * A set of MQTT constants retrieved from the System.getProperty method.
 *
 */
public final class MqttConstants {
	
	private MqttConstants(){}

	/** Hostname of the main broker */
	public static final String MQTT_BROKER_HOSTNAME = System.getProperty("org.eclipse.om2m.mqtt.ip", "127.0.0.1");
	
	/** IP of the main broker */
	public static final int MQTT_BROKER_PORT = Integer.valueOf(System.getProperty("org.eclipse.om2m.mqtt.port", "1883"));
	
	/** Username to connect to broker */
	public static final String MQTT_BROKER_USERNAME = System.getProperty("org.eclipse.om2m.mqtt.username");
	
	/** Password to connect to broker */
	public static final String MQTT_BROKER_PASSWORD = System.getProperty("org.eclipse.om2m.mqtt.password");
	
	/** MQTT Protocol prefix */
	public static final String PROTOCOL = "mqtt";
	
	/** Size of the request sender queue */
	public static final int MQTT_QUEUE_SENDER_SIZE = Integer.valueOf(System.getProperty("org.eclipse.om2m.mqtt.queue.size", "8"));
	
	/** Request pattern to parse the request topic on message reception */
	public static final Pattern REQUEST_PATTERN_IN = Pattern.compile("/oneM2M/req/([^/]+)/" + Constants.CSE_ID + "/(.*)");
	
	/** Request pattern when sending a message. */
	public static final Pattern REQUEST_PATTERN_OUT = Pattern.compile("/oneM2M/req/" + Constants.CSE_ID+ "/([^/]+)+/(.*)");
	
	/** Time out duration when waiting for a response. Unit in second. */ 
	public static final long TIME_OUT_DURATION = Long.valueOf(System.getProperty("org.eclipse.om2m.mqtt.timeout", "20"));
	
	/** MQTT format for XML in topic */
	public static final String MQTT_XML = "xml";
	
	/** MQTT format for JSON in topic */
	public static final String MQTT_JSON = "json";
	
}
