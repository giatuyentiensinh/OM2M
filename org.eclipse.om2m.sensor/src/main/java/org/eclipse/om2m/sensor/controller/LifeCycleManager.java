package org.eclipse.om2m.sensor.controller;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.sensor.RequestSender;
import org.eclipse.om2m.sensor.constants.SensorConstants;
import org.eclipse.om2m.sensor.model.Sensor;
import org.eclipse.om2m.sensor.util.ObixUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author tuyenng
 *
 */
public class LifeCycleManager {

	private static Log LOGGER = LogFactory.getLog(LifeCycleManager.class);
	private static String SIMULATION = System.getProperty(
			"org.eclipse.om2m.sensor.simulation", "true");
	private static String MAXCONTAINT = System.getProperty(
			"org.eclipse.om2m.maxNrOfInstances", "100");
	private static boolean flag = true;

	/**
	 * Handle the start of the plugin with the resource representation
	 */
	public static void start() {
		String sensorId = Sensor.TYPE;
		createSensorResources(sensorId, false, SensorConstants.POA);
		flag = true;
	}

	public static void stop() {
		System.out.println("Stop thread");
		flag = false;
	}

	/**
	 * Creates all required resources.
	 * 
	 * @param appId
	 *            - Application ID
	 * @param initValue
	 *            - initial sensor value
	 * @param poa
	 *            - sensor Point of Access
	 */
	private static void createSensorResources(String appId, boolean initValue,
			String poa) {
		// Create the Application resource
		Container container = new Container();
		container.getLabels().add("sensor");
		container.setMaxNrOfInstances(BigInteger.valueOf(0));

		AE ae = new AE();
		ae.setRequestReachability(true);
		ae.getPointOfAccess().add(poa);
		ae.setAppID(appId);

		ResponsePrimitive response = RequestSender.createAE(ae, appId);
		// Create Application sub-resources only if application not yet created
		if (response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)) {
			container = new Container();
			container.setMaxNrOfInstances(BigInteger.valueOf(Integer
					.parseInt(MAXCONTAINT)));
			// Create DESCRIPTOR container sub-resource
			LOGGER.info(RequestSender.createContainer(response.getLocation(),
					SensorConstants.DESC, container));
			// Create STATE container sub-resource
			LOGGER.info(RequestSender.createContainer(response.getLocation(),
					SensorConstants.DATA, container));
			// Create OBSERVE container coap observe
			LOGGER.info(RequestSender.createContainer(response.getLocation(),
					SensorConstants.OBSERVE, container));

			String content;
			// Create DESCRIPTION contentInstance on the DESCRIPTOR container
			// resource
			content = ObixUtil.getDescriptorRep(SensorConstants.CSE_ID, appId,
					SensorConstants.DATA);
			ContentInstance contentInstance = new ContentInstance();
			contentInstance.setContent(content);
			contentInstance.setContentInfo(MimeMediaType.OBIX);
			RequestSender.createContentInstance(SensorConstants.CSE_PREFIX
					+ "/" + appId + "/" + SensorConstants.DESC, null,
					contentInstance);
		}
		LOGGER.info("AUTO CREATE DATA SIMPLE: " + SIMULATION);
		if ("true".equals(SIMULATION)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					LOGGER.info("SEND DATA FROM SIMULATION SENSOR");
					LOGGER.info("--------------------------------");
					while (flag) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String addr = "/aaaa::212:4b00:0:2";
						int adc1 = ThreadLocalRandom.current().nextInt(1000,
								3500);
						int adc2 = ThreadLocalRandom.current().nextInt(0, 100);
						int adc3 = ThreadLocalRandom.current()
								.nextInt(300, 950);
						int batt = ThreadLocalRandom.current().nextInt(2200,
								3300);
						int temp = ThreadLocalRandom.current().nextInt(22000,
								39000);
						int tempR = ThreadLocalRandom.current().nextInt(201,
								509);
						int humiR = ThreadLocalRandom.current().nextInt(101,
								999);
						ContentInstance contentInstance = new ContentInstance();
						String content = ObixUtil.getStateRep2(Sensor.TYPE,
								addr, adc1, adc2, adc3, batt, temp, tempR,
								humiR);

						JSONObject dataObj = new JSONObject();
						try {
							dataObj.put("adc1", adc1);
							dataObj.put("adc2", adc2);
							dataObj.put("adc3", adc3);
							dataObj.put("battery", batt);
							dataObj.put("temperature", temp);
							dataObj.put("sensor_temperature", tempR);
							dataObj.put("sensor_humidity", humiR);
							dataObj.put("addr", addr);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						System.out.println(dataObj);
						ObseveSSE.sseMsg(dataObj);
						contentInstance.setContent(content);
						RequestSender.createContentInstance(
								SensorConstants.CSE_PREFIX + "/" + Sensor.TYPE
										+ "/" + SensorConstants.DATA, null,
								contentInstance);
					}
				}
			}).start();
		}
	}

}
