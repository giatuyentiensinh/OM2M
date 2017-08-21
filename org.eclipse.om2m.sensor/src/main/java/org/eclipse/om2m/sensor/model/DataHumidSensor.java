package org.eclipse.om2m.sensor.model;

import java.nio.ByteBuffer;

import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Str;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;

public class DataHumidSensor extends DataSensor {
	private final static Integer OFFSET = 10;
	private final static Integer LENGTH = 4;
	private int sensorTemperature;
	private int sensorHumidity;

	public DataHumidSensor(byte[] data, String address) {
		super(data, address);
		ByteBuffer buffer = ByteBuffer.wrap(data, OFFSET, LENGTH);
		this.sensorTemperature = converteData(buffer.getShort());
		this.sensorHumidity = converteData(buffer.getShort());
	}

	public int getSensorTemperature() {
		return sensorTemperature;
	}

	public void setSensorTemperature(int sensorTemperature) {
		this.sensorTemperature = sensorTemperature;
	}

	public int getSensorHumidity() {
		return sensorHumidity;
	}

	public void setSensorHumidity(int sensorHumidity) {
		this.sensorHumidity = sensorHumidity;
	}

	@Override
	public String toString() {
		Obj obj = new Obj();
		obj.add(new Str("addr", this.getAddress()));
		obj.add(new Int("adc1", this.getAdc1()));
		obj.add(new Int("adc2", this.getAdc2()));
		obj.add(new Int("adc3", this.getAdc3()));
		obj.add(new Int("temperature", this.getTemperature()));
		obj.add(new Int("battery", this.getBattery()));
		obj.add(new Int("sensor_temperature", this.getSensorTemperature()));
		obj.add(new Int("sensor_humidity", this.getSensorHumidity()));
		return ObixEncoder.toString(obj);
	}
}