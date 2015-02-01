package com.ithaque.funnies.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ithaque.funnies.client.FunnyService;
import com.ithaque.funnies.client.Request;
import com.ithaque.funnies.client.Response;

@SuppressWarnings("serial")
public class FunnyServiceImpl extends RemoteServiceServlet implements
		FunnyService {

	public Response invoke(Request request) throws IllegalArgumentException {
		return null;
	}
	
}
