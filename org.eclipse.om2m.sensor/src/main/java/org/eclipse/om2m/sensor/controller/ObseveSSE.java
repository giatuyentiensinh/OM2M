package org.eclipse.om2m.sensor.controller;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

import org.eclipse.om2m.sensor.constants.SensorConstants;
import org.json.JSONObject;

public class ObseveSSE {

	private static String socketio_addr = System.getProperty(
			"org.eclipse.om2m.remoteCseAddress", "127.0.0.1");
	private static String socketio_port = System.getProperty(
			"org.eclipse.om2m.sensor.socketio.port", "9092");

	private static Socket socketio = null;

	public static void start() {
		try {
			socketio = IO.socket("http://" + socketio_addr + ":"
					+ socketio_port);
			socketio.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void sseMsg(JSONObject obj) {
		socketio.emit(SensorConstants.SSE_TOPIC_MSG, obj);
	}

	public static void sseObs(JSONObject obj) {
		socketio.emit(SensorConstants.SSE_TOPIC_OBS, obj);
	}

	public static void stop() {
		socketio.close();
	}

}
