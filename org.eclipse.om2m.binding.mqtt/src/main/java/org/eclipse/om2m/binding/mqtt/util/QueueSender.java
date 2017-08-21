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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public final class QueueSender {
	
	private static final Log LOGGER = LogFactory.getLog(QueueSender.class);
	private static ExecutorService threadPool;

	static {
		int queueSize = MqttConstants.MQTT_QUEUE_SENDER_SIZE <= 2 ? 2
				: MqttConstants.MQTT_QUEUE_SENDER_SIZE;
		threadPool = new ThreadPoolExecutor(2, queueSize, 1, TimeUnit.MINUTES,
				new SynchronousQueue<Runnable>());
	}

	public static void queue(MqttClient mqttClient, String topic, byte[] payload){
		LOGGER.debug("Sending MQTT message to " + mqttClient.getServerURI() + " topic: " + topic);
		threadPool.execute(new MqttSender(mqttClient, topic, payload));
	}
	
	private static class MqttSender implements Runnable {

		private MqttClient mqttClient;
		private String topic;
		private byte[] payload;

		public MqttSender(MqttClient mqttClient, String topic, byte[] payload) {
			super();
			this.mqttClient = mqttClient;
			this.topic = topic;
			this.payload = payload;
		}

		@Override
		public void run() {
			try {
				this.mqttClient.publish(topic, payload, 1, false);
			} catch (MqttException e) {
				LOGGER.warn("Error publishing on topic: " + this.topic
						+ " of broker " + this.mqttClient.getServerURI()
						+ ". Error: " + e.getMessage());
			}
		}

	}
	
	private QueueSender(){
		// Empty and private constructor to avoid class creation
	}

}
