package org.eclipse.om2m.sensor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.exceptions.BadRequestException;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.interworking.service.InterworkingService;
import org.eclipse.om2m.sensor.coap.ControlSensor;
import org.eclipse.om2m.sensor.constants.Operations;
import org.eclipse.om2m.sensor.constants.SensorConstants;
import org.eclipse.om2m.sensor.util.ObixUtil;

/**
 * @author tuyenng
 *
 */
public class REMoteRouter implements InterworkingService {

	private static Log LOGGER = LogFactory.getLog(REMoteRouter.class);

	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		ResponsePrimitive response = new ResponsePrimitive(request);

		if (request.getQueryStrings().containsKey(
				SensorConstants.QUERY_STRING_OP)) {
			String operation = request.getQueryStrings()
					.get(SensorConstants.QUERY_STRING_OP).get(0);
			String addr = request.getQueryStrings()
					.get(SensorConstants.QUERY_STRING_ADDR).get(0);
			Operations op = Operations.getOperationFromString(operation);
			LOGGER.info("Received request in Sensor IPE: "
					+ SensorConstants.QUERY_STRING_OP + " = " + operation + ","
					+ SensorConstants.QUERY_STRING_ADDR + " = " + addr);

			String port = "5683";

			switch (op) {
			case SET_ON_LEB_RED:
				ControlSensor.controlLamp(addr, port, "r", "on");
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case SET_ON_LEB_GREEN:
				ControlSensor.controlLamp(addr, port, "g", "on");
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case SET_ON_LEB_BLUE:
				ControlSensor.controlLamp(addr, port, "b", "on");
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case SET_OFF_LEB_RED:
				ControlSensor.controlLamp(addr, port, "r", "off");
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case SET_OFF_LEB_GREEN:
				ControlSensor.controlLamp(addr, port, "g", "off");
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case SET_OFF_LEB_BLUE:
				ControlSensor.controlLamp(addr, port, "b", "off");
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case GPIO:
				ControlSensor.toggleGPIO(addr, port);
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case OBSERVEBTNACTIVE:
				ControlSensor.observeButton(addr, port);
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case OBSERVEBTNCANCEL:
				ControlSensor.observeButtonCancel(addr, port);
				response.setResponseStatusCode(ResponseStatusCode.OK);
				break;
			case GET_STATE:
				// Shall not get there...
				throw new BadRequestException();
			default:
				throw new BadRequestException();
			}
		}

		// discovery ip border router
		if (request.getQueryStrings().containsKey(
				SensorConstants.QUERY_STRING_DISCOVERY)) {
			String addr = request.getQueryStrings()
					.get(SensorConstants.QUERY_STRING_DISCOVERY).get(0);
			List<String> ips = ControlSensor.discoverySensor(addr);
			String ipsXML = ObixUtil.getListIp(ips);
			response.setContent(ipsXML);
			response.setResponseStatusCode(ResponseStatusCode.OK);
		}

		if (response.getResponseStatusCode() == null) {
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}

	@Override
	public String getAPOCPath() {
		return SensorConstants.POA;
	}

}
