package org.eclipse.om2m.sensor.util;

import java.util.List;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.ShortName;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Str;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
import org.eclipse.om2m.sensor.constants.Operations;
import org.eclipse.om2m.sensor.constants.SensorConstants;
import org.eclipse.om2m.sensor.model.Sensor;

/**
 * @author tuyenng
 *
 */
public class ObixUtil {

	/**
	 * Returns an obix XML representation describing the sensor.
	 * 
	 * @param cseId
	 *            - SclBase id
	 * @param appId
	 *            - Application Id
	 * @param stateCont
	 *            - the STATE container id
	 * @return Obix XML representation
	 */
	public static String getDescriptorRep(String cseId, String appId,
			String stateCont) {
		String prefix = cseId + "/" + Constants.CSE_NAME + "/" + appId;
		// oBIX
		Obj obj = new Obj();
		obj.add(new Str("type", Sensor.TYPE));
		obj.add(new Str("location", Sensor.LOCATION));
		obj.add(new Str("appId", appId));
		// OP GetState from SCL DataBase
		Op opState = new Op();
		opState.setName("getFristValue");
		opState.setHref(new Uri(prefix + "/" + stateCont + "/"
				+ ShortName.LATEST));
		opState.setIs(new Contract("retrieve"));
		opState.setIn(new Contract("obix:Nil"));
		opState.setOut(new Contract("obix:Nil"));
		obj.add(opState);

		// OP Execute set on red leb
		Op onLebRed = new Op();
		onLebRed.setName(Operations.SET_ON_LEB_RED.getValue());
		onLebRed.setHref(new Uri(prefix + "?" + SensorConstants.QUERY_STRING_OP
				+ "=" + Operations.SET_ON_LEB_RED + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		onLebRed.setIs(new Contract("execute"));
		onLebRed.setIn(new Contract("obix:Nil"));
		onLebRed.setOut(new Contract("obix:Nil"));
		obj.add(onLebRed);
		// OP Execute set on green leb
		Op onLebGreen = new Op();
		onLebGreen.setName(Operations.SET_ON_LEB_GREEN.getValue());
		onLebGreen.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_OP + "="
				+ Operations.SET_ON_LEB_GREEN + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		onLebGreen.setIs(new Contract("execute"));
		onLebGreen.setIn(new Contract("obix:Nil"));
		onLebGreen.setOut(new Contract("obix:Nil"));
		obj.add(onLebGreen);
		// OP Execute set on blue leb
		Op onLebBlue = new Op();
		onLebBlue.setName(Operations.SET_ON_LEB_BLUE.getValue());
		onLebBlue.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_OP + "="
				+ Operations.SET_ON_LEB_BLUE + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		onLebBlue.setIs(new Contract("execute"));
		onLebBlue.setIn(new Contract("obix:Nil"));
		onLebBlue.setOut(new Contract("obix:Nil"));
		obj.add(onLebBlue);

		// OP Execute set off red leb
		Op offLebRed = new Op();
		offLebRed.setName(Operations.SET_OFF_LEB_RED.getValue());
		offLebRed.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_OP + "="
				+ Operations.SET_OFF_LEB_RED + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		offLebRed.setIs(new Contract("execute"));
		offLebRed.setIn(new Contract("obix:Nil"));
		offLebRed.setOut(new Contract("obix:Nil"));
		obj.add(offLebRed);
		// OP Execute set off green leb
		Op offLebGreen = new Op();
		offLebGreen.setName(Operations.SET_OFF_LEB_GREEN.getValue());
		offLebGreen.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_OP + "="
				+ Operations.SET_OFF_LEB_GREEN + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		offLebGreen.setIs(new Contract("execute"));
		offLebGreen.setIn(new Contract("obix:Nil"));
		offLebGreen.setOut(new Contract("obix:Nil"));
		obj.add(offLebGreen);
		// OP Execute set off blue leb
		Op offLebBlue = new Op();
		offLebBlue.setName(Operations.SET_OFF_LEB_BLUE.getValue());
		offLebBlue.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_OP + "="
				+ Operations.SET_OFF_LEB_BLUE + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		offLebBlue.setIs(new Contract("execute"));
		offLebBlue.setIn(new Contract("obix:Nil"));
		offLebBlue.setOut(new Contract("obix:Nil"));
		obj.add(offLebBlue);

		// discovery ips
		Op ips = new Op();
		ips.setName("discovery ips");
		ips.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_DISCOVERY + "="
				+ "aaaa::212:4b00:9df:4f53"));
		ips.setIs(new Contract("execute"));
		ips.setIn(new Contract("obix:Nil"));
		ips.setOut(new Contract("obix:Nil"));
		obj.add(ips);

		// gpio
		Op gpio = new Op();
		gpio.setName("GPIO toggle PA4");
		gpio.setHref(new Uri(prefix + "?" + SensorConstants.QUERY_STRING_OP
				+ "=" + Operations.GPIO + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		gpio.setIs(new Contract("execute"));
		gpio.setIn(new Contract("obix:Nil"));
		gpio.setOut(new Contract("obix:Nil"));
		obj.add(gpio);

		// observe btn
		Op observeBtnActive = new Op();
		observeBtnActive.setName("observeBtnActive");
		observeBtnActive.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_OP + "="
				+ Operations.OBSERVEBTNACTIVE + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		observeBtnActive.setIs(new Contract("execute"));
		observeBtnActive.setIn(new Contract("obix:Nil"));
		observeBtnActive.setOut(new Contract("obix:Nil"));
		obj.add(observeBtnActive);

		// observe btn cancel
		Op observeBtnCancel = new Op();
		observeBtnCancel.setName("observeBtnCancel");
		observeBtnCancel.setHref(new Uri(prefix + "?"
				+ SensorConstants.QUERY_STRING_OP + "="
				+ Operations.OBSERVEBTNCANCEL + "&"
				+ SensorConstants.QUERY_STRING_ADDR + "="
				+ SensorConstants.ADDR_SENSOR_EXAMPLE));
		observeBtnCancel.setIs(new Contract("execute"));
		observeBtnCancel.setIn(new Contract("obix:Nil"));
		observeBtnCancel.setOut(new Contract("obix:Nil"));
		obj.add(observeBtnCancel);

		return ObixEncoder.toString(obj);
	}

	/**
	 * Returns an obix XML representation describing the current state.
	 * 
	 * @return Obix XML representation
	 */

	public static String getStateRep(String sensorId, String addr, int adc1,
			int adc2, int adc3, int batt, int temp) {
		// oBIX
		Obj obj = new Obj();
		obj.add(new Str("addr", addr));
		obj.add(new Int("adc1", adc1));
		obj.add(new Int("adc2", adc2));
		obj.add(new Int("adc3", adc3));
		obj.add(new Int("temperature", temp));
		obj.add(new Int("battery", batt));
		return ObixEncoder.toString(obj);
	}

	public static String getStateRep2(String sensorId, String addr, int adc1,
			int adc2, int adc3, int batt, int temp, int tempR, int humiR) {
		// oBIX
		Obj obj = new Obj();
		obj.add(new Str("addr", addr));
		obj.add(new Int("adc1", adc1));
		obj.add(new Int("adc2", adc2));
		obj.add(new Int("adc3", adc3));
		obj.add(new Int("temperature", temp));
		obj.add(new Int("battery", batt));
		obj.add(new Int("sensor_temperature", tempR));
		obj.add(new Int("sensor_humidity", humiR));
		return ObixEncoder.toString(obj);
	}

	/**
	 * Return an obix XML presenstation list ip
	 * 
	 * @param ips
	 *            - List ip
	 * @return - Obix XML representation
	 */
	public static String getListIp(List<String> ips) {
		if (ips == null)
			return ObixEncoder.toString(null);
		Obj obj = new Obj();
		for (String ip : ips) {
			obj.add(new Str("ip", ip));
		}
		return ObixEncoder.toString(obj);
	}
}
