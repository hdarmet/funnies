package com.ithaque.funnies.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface FunnyServiceAsync {
	void invoke(Request request, AsyncCallback<Response> callback) throws IllegalArgumentException;
}
