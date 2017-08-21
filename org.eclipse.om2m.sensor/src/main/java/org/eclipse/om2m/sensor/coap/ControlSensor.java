package org.eclipse.om2m.sensor.coap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Str;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.sensor.RequestSender;
import org.eclipse.om2m.sensor.constants.SensorConstants;
import org.eclipse.om2m.sensor.controller.ObseveSSE;
import org.eclipse.om2m.sensor.model.Sensor;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author tuyenng
 *
 */
public class ControlSensor {

	private static CoapObserveRelation clientobs;

	private final static String BLUECOLOR = "\033[1;34m";
	private final static String RESETCOLOR = "\033[0m";

	/**
	 * Control lamp sensor RE-Mote
	 * 
	 * @param addr
	 *            - IPv6. e.g: aaaa::c30c:0:0:2
	 * @param port
	 *            - port sensor. e.g: 5683
	 * @param color
	 *            - r|g|b
	 * @param status
	 *            - on|off
	 */
	public static void controlLamp(String addr, String port, String color,
			String status) {
		System.out.println(BLUECOLOR);
		CoapClient client = new CoapClient("coap://[" + addr + "]:" + port
				+ "/actuators/leds?color=" + color);
		CoapResponse post = client.post("mode=" + status,
				MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(post.getCode());
		System.out.println("---------------------------------");
		System.out.println("addr   : " + addr);
		System.out.println("color  : " + color);
		System.out.println("status : " + status);
		System.out.println("---------------------------------");
		System.out.println(RESETCOLOR);
	}

	public static void toggleGPIO(String addr, String port) {
		System.out.println(BLUECOLOR);
		CoapClient client = new CoapClient("coap://[" + addr + "]:" + port
				+ "/sensors/gpio");
		CoapResponse post = client.post("", MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(post.getCode());
		System.out.println(RESETCOLOR);
	}

	public static String observeButton(String addr, String port) {
		CoapClient client = new CoapClient("coap://[" + addr + "]:" + port
				+ "/test/event_button");
		clientobs = client.observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse res) {
				String context = res.getResponseText();
				System.out.println(BLUECOLOR);
				System.out.println("NOTIFICATION: " + context);
				System.out.println("RES:"
						+ clientobs.getCurrent().getResponseText());
				System.out.println(RESETCOLOR);

				ContentInstance contentInstance = new ContentInstance();
				Obj obj = new Obj();
				obj.add(new Str("tick", context));
				contentInstance.setContent(ObixEncoder.toString(obj));

				JSONObject jobj = new JSONObject();
				try {
					jobj.put("tick", context);
					ObseveSSE.sseObs(jobj);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				RequestSender.createContentInstance(SensorConstants.CSE_PREFIX
						+ "/" + Sensor.TYPE + "/" + SensorConstants.OBSERVE,
						null, contentInstance);
			}

			@Override
			public void onError() {
				System.err.println("OBSERVING FAILED (press enter to exit)");
			}
		});

		return null;
	}

	public static void observeButtonCancel(String addr, String port) {
		clientobs.proactiveCancel();
	}

	public static List<String> discoverySensor(String addr) {
		List<String> ips = new ArrayList<String>();
		try {
			Document doc = Jsoup.connect("http://[" + addr + "]/").get();
			Elements pres = doc.getElementsByTag("strong");
			System.out.println("---------------------------------");
			for (Element pre : pres) {
				String ip = pre.text();
				ips.add(ip);
				System.out.println("ipv6: " + ip);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return ips;
	}
}
