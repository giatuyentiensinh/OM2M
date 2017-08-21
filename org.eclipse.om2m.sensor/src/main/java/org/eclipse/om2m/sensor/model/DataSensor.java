package org.eclipse.om2m.sensor.model;

import java.nio.ByteBuffer;

import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Str;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;

public class DataSensor {

	private int adc1;
	private int adc2;
	private int adc3;
	private int temperature;
	private int battery;
	private String address;

	public DataSensor(byte[] data, String address) {
		try {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			this.adc1 = converteData(buffer.getShort());
			this.adc2 = converteData(buffer.getShort());
			this.adc3 = converteData(buffer.getShort());
			this.temperature = converteData(buffer.getShort());
			this.battery = converteData(buffer.getShort());
		} catch (Exception exp) {
			System.out.println("Exception: " + exp);
			this.adc1 = this.adc2 = this.adc3 = this.temperature = this.battery = 0;
		}
		this.address = address;
	}

	public int getAdc1() {
		return adc1;
	}

	public void setAdc1(int adc1) {
		this.adc1 = adc1;
	}

	public int getAdc2() {
		return adc2;
	}

	public void setAdc2(int adc2) {
		this.adc2 = adc2;
	}

	public int getAdc3() {
		return adc3;
	}

	public void setAdc3(int adc3) {
		this.adc3 = adc3;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Kéo dài bit thành 16 bit. e.g: 1101 -> 0000 0000 0000 1101
	 */
	protected static int converteData(short value) {
		return value & 0xffff;
	}

	@Override
	public String toString() {
		Obj obj = new Obj();
		obj.add(new Str("addr", this.address));
		obj.add(new Int("adc1", this.adc1));
		obj.add(new Int("adc2", this.adc2));
		obj.add(new Int("adc3", this.adc3));
		obj.add(new Int("temperature", this.temperature));
		obj.add(new Int("battery", this.battery));
		return ObixEncoder.toString(obj);
	}

}
