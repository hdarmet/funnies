package com.ithaque.funnies.shared.funny.manager;

import java.util.HashMap;
import java.util.Map;

import com.ithaque.funnies.shared.funny.Sketch;

public abstract class AbstracCircusManager implements CircusManager {

	Map<Class<? extends Notification>, Handler<? extends Notification>> handlers = 
			new HashMap<Class<? extends Notification>, Handler<? extends Notification>>();
		
	public static abstract class Handler<T extends Notification> {
		public Handler(Class<T> notificationClass, AbstracCircusManager manager) {
			manager.handlers.put(notificationClass, this);
		}

		public abstract Sketch process(T notification);
	}
		
	@Override
	public <T extends Notification> Sketch process(T notification) {
		@SuppressWarnings("unchecked")
		Handler<T> handler = (Handler<T>)handlers.get(notification.getClass());
		if (handler!=null) {
			return handler.process(notification);
		}
		else {
			return null;
		}
	}
	
}
