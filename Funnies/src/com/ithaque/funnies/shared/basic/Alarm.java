package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.basic.Event.Type;

public class Alarm {

	long timeout;
	boolean repeat;
	String alarmId;
	long startTime = 0;
	long endTime = -1;
	boolean finished = false;
	
	public Alarm(long timeout, boolean repeat, String alarmId) {
		this.timeout = timeout;
		this.repeat = repeat;
		this.alarmId = alarmId;
	}
	
	public Event process(long time) {
		if (startTime == 0) {
			startTime = time;
			endTime = startTime + timeout;
		}
		else {
			if (endTime<=time) {
				if (repeat) {
					startTime=endTime;
					endTime+=timeout;
				}
				else {
					finished = true;
				}
				return new AlarmEvent(Type.ALARM, alarmId, time);
			}
		}
		return null;
	}

	public boolean isFinished() {
		return finished;
	}
}
