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

import java.math.BigInteger;
import java.util.regex.Matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.binding.mqtt.util.DataMapperRegistry;
import org.eclipse.om2m.binding.mqtt.util.MqttConstants;
import org.eclipse.om2m.binding.mqtt.util.QueueSender;
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.PrimitiveContent;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.datamapping.service.DataMapperService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * MQTT Request Handler class that subscribe to oneM2M request topic.
 * When a request is received in the request topic, it is de-serialized and send
 * to the CseService implementation available. Then the response from the service
 * is serialized and sent to the oneM2M response topic.
 */
public class MqttRequestHandler implements MqttCallback {

	// Static attributes of the class 
	
	/** MQTT Client ID */
	private static final String CLIENT_ID = Constants.CSE_ID;
	/** Logger reference */
	private static final Log LOGGER = LogFactory.getLog(MqttRequestHandler.class);
	/** MQTT Request Topic */
	private static final String REQUEST_TOPIC = "/oneM2M/req/+/" + Constants.CSE_ID + "/+";
	

	/** Reference to the current cseService implementation*/
	private static CseService cseService;

	/**
	 * Set the current CseService used when a request is 
	 * received on the oneM2M request topic.
	 * @param cse the CseService implementation to use
	 */
	public static void setCseService(CseService cse) {
		cseService = cse;
	}

	// Private attributes
	
	/** MQTT Client from the Paho library */
	private MqttClient mainMqttClient;

	/** The MQTT connection options to use */
	private MqttConnectOptions connOpts;
	
	/** Tell the thread to keep retrying or not */
	private boolean retry = true;
	/** Connection retry thread */
	private Thread retryThread;
	
	/**
	 * Default constructor of the Request Handler
	 */
	public MqttRequestHandler() {
		MemoryPersistence persistence = new MemoryPersistence();
		String url = "tcp://" + MqttConstants.MQTT_BROKER_HOSTNAME + ":"
				+ MqttConstants.MQTT_BROKER_PORT;
		this.connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		if(MqttConstants.MQTT_BROKER_USERNAME != null && MqttConstants.MQTT_BROKER_PASSWORD != null){
			connOpts.setUserName(MqttConstants.MQTT_BROKER_USERNAME);
			connOpts.setPassword(MqttConstants.MQTT_BROKER_PASSWORD.toCharArray());
		}
		try {
			LOGGER.debug("Connecting MQTT client to: " + url);
			this.mainMqttClient = new MqttClient(url, CLIENT_ID, persistence);
			this.mainMqttClient.setCallback(MqttRequestHandler.this);
			this.connect(this.connOpts);
		} catch (MqttException e) {
			LOGGER.error("Error in MQTT Client creation", e);
		}
	}
	
	/**
	 * Connect and retry if the connection fails
	 * @param connOpts
	 */
	private void connect(final MqttConnectOptions connOpts){
		if(retry && retryThread == null){
			retryThread = new Thread("mqtt-connection-retrier"){
				public void run() {
					while(retry && !MqttRequestHandler.this.mainMqttClient.isConnected()){
						try {
							MqttRequestHandler.this.mainMqttClient.connect(connOpts);
							
							LOGGER.info("Subscribing on MQTT topic: " + REQUEST_TOPIC);
							MqttRequestHandler.this.mainMqttClient.subscribe(REQUEST_TOPIC, 2);
						} catch (MqttException e) {
							LOGGER.warn("Cannot connect to MQTT Broker, retrying in 10s. Cause: " + e.getMessage());
						}
						if(!MqttRequestHandler.this.mainMqttClient.isConnected()){
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// Ignore
							}
						}
					}
					MqttRequestHandler.this.retryThread = null;
				};
			};
		}
		retryThread.start();
	}

	@Override
	public void connectionLost(Throwable throwable) {
		LOGGER.warn("Connection lost on MQTT Broker at " + MqttConstants.MQTT_BROKER_HOSTNAME + ":" + MqttConstants.MQTT_BROKER_PORT);
		this.connect(this.connOpts);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// Empty
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		Matcher matcher = MqttConstants.REQUEST_PATTERN_IN.matcher(topic);
		if (matcher.matches()) {
			String aeId = matcher.group(1);
			String format = matcher.group(2);
			String responseTopic = "/oneM2M/resp/" + Constants.CSE_ID + "/" + aeId + "/" + format;
			
			if (message.getPayload() == null) {
				LOGGER.info("Null message received on " + topic);
				sendErrorResponse("The message is null", responseTopic, aeId, format);
				return;
			}
			String payload = new String(message.getPayload());
			LOGGER.debug("(" + topic + ") Message received (qos: " + message.getQos() + "):\n" + payload);
			DataMapperService dms = DataMapperRegistry.getFromMqttFormat(format);

			if (dms == null) {
				LOGGER.warn("MQTT Request received with unhandled content type: " + format);
				sendErrorResponse("The format type is not handled", 
						responseTopic.replace("/" + format, "/" + MqttConstants.MQTT_XML), 
						aeId, MqttConstants.MQTT_XML);
				return;
			}
			Object objectPayload = dms.stringToObj(payload);
			if(objectPayload == null || !(objectPayload instanceof RequestPrimitive)){
				LOGGER.info("Invalid content provided in MQTT request");
				sendErrorResponse("Invalid content provided in request primitive", responseTopic, aeId, format);
				return;
			}
			RequestPrimitive requestPrimitive = (RequestPrimitive) objectPayload;
			requestPrimitive.setRequestContentType(MimeMediaType.OBJ);
			requestPrimitive.setReturnContentType(MimeMediaType.OBJ);
			// Primitive content handling
			if(requestPrimitive.getPrimitiveContent() != null && 
					!requestPrimitive.getPrimitiveContent().getAny().isEmpty() && 
					requestPrimitive.getContent() == null){
				requestPrimitive.setContent(requestPrimitive.getPrimitiveContent().getAny().get(0));
			}
			
			ResponsePrimitive responsePrimitive = null;
			
			if(cseService != null){
				// Sending the request to the CSE
				responsePrimitive = cseService.doRequest(requestPrimitive);
				
				// Handling the custom "content" field and map it to PrimitiveContent for serialization
				if(responsePrimitive.getContent() != null && 
						responsePrimitive.getPrimitiveContent() == null){
					PrimitiveContent pc = new PrimitiveContent();
					pc.getAny().add(responsePrimitive.getContent());
					responsePrimitive.setPrimitiveContent(pc);
				}
				
				// Building and sending response
				final String responsePayload = dms.objToString(responsePrimitive);
				LOGGER.debug("Response to be sent on topic: " + responseTopic + ". Payload:\n" + responsePayload);
				
				// Sending the request in another thread otherwise it blocks the reception thread of Paho
				QueueSender.queue(mainMqttClient, responseTopic, responsePayload.getBytes());
			} else {
				sendErrorResponse("/" + Constants.CSE_ID + " is not available", responseTopic, aeId, format, ResponseStatusCode.SERVICE_UNAVAILABLE);
			}

		} else {
			LOGGER.debug("The topic is not well formed. (" + topic + ")");
		}
	}
	
	/**
	 * Util method that send an error message to the client
	 * @param message the message to send
	 * @param responseTopic the response topic to reply on
	 * @param aeId the id of the client
	 * @param format the format of exchange
	 */
	private void sendErrorResponse(String message, String responseTopic,
			String aeId, String format, BigInteger responseStatusCode) {
		ResponsePrimitive responsePrimitive = new ResponsePrimitive();
		responsePrimitive.setTo(aeId);
		responsePrimitive.setFrom("/" + Constants.CSE_ID);
		responsePrimitive.setResponseStatusCode(responseStatusCode);
		responsePrimitive.setPrimitiveContent(new PrimitiveContent());
		responsePrimitive.getPrimitiveContent().getAny().add(message);
		DataMapperService dms = DataMapperRegistry.getFromMqttFormat(format);
		byte[] errorPayload = dms.objToString(responsePrimitive).getBytes();
		QueueSender.queue(mainMqttClient, responseTopic, errorPayload);
	}
	
	/**
	 * Send a bad request response to the client 
	 * @param message the message
	 * @param responseTopic the response topic
	 * @param aeId the id of the client
	 * @param format the format of exchange
	 */
	private void sendErrorResponse(String message, String responseTopic,
			String aeId, String format){
		sendErrorResponse(message, responseTopic, aeId, format, ResponseStatusCode.BAD_REQUEST);
	}

	/**
	 * Disconnecting and closing the MQTT Client.
	 */
	public void close(){
		// Disconnect the MQTT Client
		try {
			this.mainMqttClient.disconnect();
		} catch (MqttException e) {
			LOGGER.debug("Error disconnecting the MQTT Client", e);
		}
		// Prevent on any reconnection retry
		retry = false;
		// Wake up the retry thread after the false on "retry" has been set
		if(retryThread != null && retryThread.isAlive()){
			retryThread.interrupt();
		}
	}
}
