package org.eclipse.om2m.sensor.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.sensor.RequestSender;
import org.eclipse.om2m.sensor.constants.SensorConstants;
import org.eclipse.om2m.sensor.model.DataHumidTempSensor;
import org.eclipse.om2m.sensor.model.DataSensor;
import org.eclipse.om2m.sensor.model.Sensor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author tuyenng
 */
/**
 * Ham doc du lieu tu cac sensor RE-Mote zoleria
 */
public class ReceiverDataSensor {

	private static DatagramSocket socket = null;

	/**
	 * Bat dau mo socket va nhan format du lieu
	 * 
	 * @param port
	 */
	public static void start(int port) {

		try {
			JSONObject dataObj = new JSONObject();

			socket = new DatagramSocket(port);
			while (true) {
				byte[] recvData = new byte[1024];
				DatagramPacket recvPacket = new DatagramPacket(recvData,
						recvData.length);
				socket.receive(recvPacket);
				byte[] dataByte = recvPacket.getData();

				int count = 0;
				for (int i = dataByte.length - 1; i >= 0; i--) {
					if (dataByte[i] != 0)
						break;
					count++;
				}

				DataSensor dataSensor = null;
				if (1024 - count == 10) {
					dataSensor = new DataSensor(dataByte, recvPacket
							.getAddress().toString()) {
					};
				} else if (1024 - count == 14) {
					DataHumidTempSensor dataSensor2 = new DataHumidTempSensor(dataByte, recvPacket
							.getAddress().toString());
					dataSensor = dataSensor2;
					System.out.println(dataSensor);
					
					try {
						dataObj.put("sensor_temperature",dataSensor2.getSensorTemperature());
						dataObj.put("sensor_humidity", dataSensor2.getSensorHumidity());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				try {
					dataObj.put("adc1", dataSensor.getAdc1());
					dataObj.put("adc2", dataSensor.getAdc2());
					dataObj.put("adc3", dataSensor.getAdc3());
					dataObj.put("battery", dataSensor.getBattery());
					dataObj.put("temperature", dataSensor.getTemperature());
					dataObj.put("addr", dataSensor.getAddress());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				ObseveSSE.sseMsg(dataObj);

				System.out.println(dataSensor);
				ContentInstance contentInstance = new ContentInstance();
				contentInstance.setContent(dataSensor.toString());
				RequestSender.createContentInstance(SensorConstants.CSE_PREFIX
						+ "/" + Sensor.TYPE + "/" + SensorConstants.DATA, null,
						contentInstance);
			}
		} catch (SocketException e) {
			e.printStackTrace();
			stop();
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
	}

	/**
	 * Dong socket
	 */
	public static void stop() {
		if (!socket.isClosed()) {
			socket.close();
		}
	}

}
