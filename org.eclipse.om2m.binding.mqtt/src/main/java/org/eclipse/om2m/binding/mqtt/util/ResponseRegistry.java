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
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.datamapping.service.DataMapperService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public final class ResponseRegistry {

	private ResponseRegistry() {
		// Empty and private constructor to avoid instantiation of this class
	}
	
	private static final Map<String, ResponseSemaphore> responseMap = new HashMap<String, ResponseSemaphore>();

	public static ResponseSemaphore createSemaphore(String requestIdentifier, MqttClient mqttClient, 
			String responseTopic) throws MqttException{
		synchronized (responseMap) {
			mqttClient.setCallback(new ResponseCallback());
			mqttClient.subscribe(responseTopic, 1);
			if(!responseMap.containsKey(requestIdentifier)){
				ResponseSemaphore respSemaphore = new ResponseSemaphore(new Semaphore(0));
				responseMap.put(requestIdentifier, respSemaphore);
				return respSemaphore;
			}
			return null;
		}
	}
	
	private static void responseReceived(ResponsePrimitive responsePrimitive){
		synchronized (responseMap) {
			if(responseMap.containsKey(responsePrimitive.getRequestIdentifier())){
				ResponseSemaphore responseSemanphore = responseMap.get(responsePrimitive.getRequestIdentifier());
				responseSemanphore.setResponsePrimitive(responsePrimitive);
				responseSemanphore.getSemaphore().release();
				responseMap.remove(responsePrimitive.getRequestIdentifier());
			}			
		}
	}
	
	private static class ResponseCallback implements MqttCallback {

		private static Pattern responsePattern = Pattern.compile("/oneM2M/resp/([^/]+)/" + Constants.CSE_ID + "/(.*)");

		@Override
		public void connectionLost(Throwable cause) {
			// Ignore
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			// Ignore
		}

		@Override
		public void messageArrived(String topic, MqttMessage message)
				throws Exception {
			Matcher matcher = responsePattern.matcher(topic);
			if(!matcher.matches()){
				return;
			}
			String format = matcher.group(2);
			DataMapperService dms = null;
			switch (format) {
			case "xml":
				dms = DataMapperRegistry.get(MimeMediaType.XML);
				break;
			case "json":
				dms = DataMapperRegistry.get(MimeMediaType.JSON);
				break;
			default:
				break;
			}
			if(dms == null){
				// The format is not handled here
				return;
			}
			String payload = new String(message.getPayload());
			ResponsePrimitive resp = (ResponsePrimitive) dms.stringToObj(payload);
			if(resp == null || resp.getRequestIdentifier() == null){
				return;
			}
			responseReceived(resp);
		}
		
	}
	
}
