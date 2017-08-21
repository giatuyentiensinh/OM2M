package org.eclipse.om2m.sensor.model;

/**
 * @author tuyenng
 *
 */
public class Sensor {

	/** Default Sensors location */
	public final static String LOCATION = "IoT workshop";
	/** Default Sensors type */
	public final static String TYPE = "RE-Mote";
	/** Sensor state */
	private boolean state = false;
	/** Sensor ID */
	private String sensorId;

	public Sensor(boolean state, String sensorId) {
		super();
		this.state = state;
		this.sensorId = sensorId;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

}
