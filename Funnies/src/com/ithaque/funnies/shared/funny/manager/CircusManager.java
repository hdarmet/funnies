package com.ithaque.funnies.shared.funny.manager;

import java.util.HashMap;
import java.util.Map;

public abstract class CircusManager {

	Map<Class<? extends Notification>, Handler<? extends Notification>> handlers = 
			new HashMap<Class<? extends Notification>, Handler<? extends Notification>>();
		
	public static abstract class Handler<T extends Notification> {
		public Handler(Class<T> notificationClass, CircusManager manager) {
			manager.handlers.put(notificationClass, this);
		}

		public abstract void process(T fact);
	}
		
	public <T extends Notification> void process(T fact) {
		@SuppressWarnings("unchecked")
		Handler<T> handler = (Handler<T>)handlers.get(fact.getClass());
		if (handler!=null) {
			handler.process(fact);
		}
	}
	
}
