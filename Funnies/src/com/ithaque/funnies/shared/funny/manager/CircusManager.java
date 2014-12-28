package com.ithaque.funnies.shared.funny.manager;

import com.ithaque.funnies.shared.funny.Sketch;

public interface CircusManager {

	public <T extends Notification> Sketch process(T fact);

}
