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
package org.eclipse.om2m.binding.mqtt;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.binding.mqtt.util.DataMapperRegistry;
import org.eclipse.om2m.binding.mqtt.util.MqttConstants;
import org.eclipse.om2m.binding.mqtt.util.QueueSender;
import org.eclipse.om2m.binding.mqtt.util.ResponseRegistry;
import org.eclipse.om2m.binding.mqtt.util.ResponseSemaphore;
import org.eclipse.om2m.binding.service.RestClientService;
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.PrimitiveContent;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.datamapping.service.DataMapperService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttRestClient implements RestClientService {
	
	private static final Log LOGGER = LogFactory.getLog(MqttRestClient.class);
	
	@Override
	public ResponsePrimitive sendRequest(RequestPrimitive requestPrimitive) {
		if(requestPrimitive.getContent() != null){
			PrimitiveContent pc = new PrimitiveContent();
			switch(requestPrimitive.getRequestContentType()){
			case MimeMediaType.XML:
				pc.getAny().add(DataMapperRegistry.get(MimeMediaType.XML).stringToObj((String)requestPrimitive.getContent()));
				break;
			case MimeMediaType.JSON:
				pc.getAny().add(DataMapperRegistry.get(MimeMediaType.JSON).stringToObj((String)requestPrimitive.getContent()));
				break;
			case MimeMediaType.OBJ: case MimeMediaType.TEXT_PLAIN:
				pc.getAny().add(requestPrimitive.getContent());
				break;
			default:
				break;
			}
			if(!pc.getAny().isEmpty()){
				requestPrimitive.setPrimitiveContent(pc);
			}
		}
		
		ResponsePrimitive responsePrimitive = new ResponsePrimitive(requestPrimitive);

		if(requestPrimitive.getMqttTopic() == null || requestPrimitive.getMqttUri() == null){
			responsePrimitive.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			return responsePrimitive;
		}
		
		if(requestPrimitive.getRequestIdentifier() == null){
			requestPrimitive.setRequestIdentifier(UUID.randomUUID().toString());
		}
		
		String uri = requestPrimitive.getMqttUri();
		if(uri.startsWith("mqtt://")){
			uri = uri.replaceFirst("mqtt://", "tcp://");
		}
		
		if(requestPrimitive.getTo().startsWith("mqtt://")){
			Pattern mqttUriPatter = Pattern.compile("(mqtt://[^:/]*(:[0-9]{1,5})?)(/.*)");
			Matcher matcher = mqttUriPatter.matcher(requestPrimitive.getTo());
			if(matcher.matches()){
				requestPrimitive.setTo(matcher.group(3));
			}
		}
		
		String topic = requestPrimitive.getMqttTopic();
		String payload = null;
		String format = null;
		if (topic.endsWith("/json")){
			payload = DataMapperRegistry.get(MimeMediaType.JSON).objToString(requestPrimitive);
			format = "json";
		} else {
			// Case of XML and default
			payload = DataMapperRegistry.get(MimeMediaType.XML).objToString(requestPrimitive);
			format = "xml";
		}
		
		try {
			MqttClient mqttClient = new MqttClient(uri, requestPrimitive.getRequestIdentifier(), new MemoryPersistence());
			mqttClient.connect();
			LOGGER.debug("Sending request on topic: " + topic + " with payload:\n" + payload);
			ResponseSemaphore responseSemaphore = null;
			if(requestPrimitive.isMqttResponseExpected()){
				Matcher matcher = MqttConstants.REQUEST_PATTERN_OUT.matcher(topic);
				if(matcher.matches()){
					String responseTopic = "/oneM2M/resp/" + matcher.group(1) + "/"+ Constants.CSE_ID + "/" + format; 
					responseSemaphore = ResponseRegistry.createSemaphore(requestPrimitive.getRequestIdentifier(), mqttClient, responseTopic);
				} else {					
					responsePrimitive.setResponseStatusCode(ResponseStatusCode.TARGET_NOT_REACHABLE);
				}
			} else {
				mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
				responsePrimitive.setResponseStatusCode(ResponseStatusCode.OK);
			} 
			if(responseSemaphore != null){
				QueueSender.queue(mqttClient, topic, payload.getBytes());
				LOGGER.debug("Waiting for response... (" + MqttConstants.TIME_OUT_DURATION + "s)");
				boolean released = responseSemaphore.getSemaphore().tryAcquire(1, MqttConstants.TIME_OUT_DURATION, TimeUnit.SECONDS);
				if(released){
					responsePrimitive = responseSemaphore.getResponsePrimitive();
					fillAndConvertContent(requestPrimitive, responsePrimitive);
					LOGGER.debug("Response received: " + responsePrimitive);
				} else {
					responsePrimitive.setResponseStatusCode(ResponseStatusCode.TARGET_NOT_REACHABLE);
				}
			}
			mqttClient.disconnect();
			mqttClient.close();
		} catch (MqttException e) {
			LOGGER.warn("Cannot connect to: " + requestPrimitive.getMqttUri());
			responsePrimitive.setResponseStatusCode(ResponseStatusCode.TARGET_NOT_REACHABLE);
			return responsePrimitive;
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted exception caught in MqttRestClient: " + e.getMessage());
			responsePrimitive.setResponseStatusCode(ResponseStatusCode.TARGET_NOT_REACHABLE);
			return responsePrimitive;
		}
		
		return responsePrimitive;
	}

	private void fillAndConvertContent(RequestPrimitive requestPrimitive,
			ResponsePrimitive responsePrimitive) {
		if(responsePrimitive.getPrimitiveContent() != null && 
				!responsePrimitive.getPrimitiveContent().getAny().isEmpty() && 
				responsePrimitive.getContent() == null){
			if(requestPrimitive.getReturnContentType().equals(MimeMediaType.OBJ)){
				responsePrimitive.setContent(responsePrimitive.getPrimitiveContent().getAny().get(0));				
			} else {
				DataMapperService dms = DataMapperRegistry.get(requestPrimitive.getReturnContentType()); 
				String content = dms.objToString(responsePrimitive.getPrimitiveContent().getAny().get(0));
				responsePrimitive.setContent(content);
				responsePrimitive.setContentType(requestPrimitive.getReturnContentType());
			}
		}
	}

	@Override
	public String getProtocol() {
		return MqttConstants.PROTOCOL;
	}	
	
}
