package com.ithaque.funnies.shared.basic;


public class AlarmEvent extends Event {

	String alarm;
	long time;
	
	public AlarmEvent(Type type, String alarm, long time) {
		super(type);
		this.time = time;
		this.alarm = alarm;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getAlarm() {
		return alarm;
	}
}
