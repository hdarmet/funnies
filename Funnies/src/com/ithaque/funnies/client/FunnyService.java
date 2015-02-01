package com.ithaque.funnies.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("funny")
public interface FunnyService extends RemoteService {
	Response invoke(Request request) throws IllegalArgumentException;
}
