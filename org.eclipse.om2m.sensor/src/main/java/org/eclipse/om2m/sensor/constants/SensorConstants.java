package org.eclipse.om2m.sensor.constants;

import org.eclipse.om2m.commons.constants.Constants;

/**
 * @author tuyenng
 *
 */
public class SensorConstants {

	public static final String POA = "remote";
	public static final String OBSERVE = "OBSERVE";
	public static final String DATA = "DATA";
	public static final String DESC = "DESCRIPTOR";
	public static final String AE_NAME = "AE_SENSOR_SAMPLE";
	public static final String SENSOR = "SENSOR";
	public static final String QUERY_STRING_OP = "op";
	public static final String QUERY_STRING_ADDR = "addr";
	public static final String ADDR_SENSOR_EXAMPLE = "aaaa::212:4b00:615:a974";

	public static final String SSE_TOPIC_MSG = "msg";
	public static final String SSE_TOPIC_OBS = "obj";

	public static final String QUERY_STRING_DISCOVERY = "discovery";
	public static String CSE_ID = "/" + Constants.CSE_ID;
	public static String CSE_PREFIX = CSE_ID + "/" + Constants.CSE_NAME;
}
