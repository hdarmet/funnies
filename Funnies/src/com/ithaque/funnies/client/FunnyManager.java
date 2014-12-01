package com.ithaque.funnies.client;

import com.ithaque.funnies.shared.funny.manager.CircusManager;
import com.ithaque.funnies.shared.funny.manager.DropRequest;

public class FunnyManager implements CircusManager {

	@Override
	public Response process(Request request) {
		if (request instanceof DropRequest) {
			DropRequest dropRequest = (DropRequest)request;
			System.out.println("Drop : "+dropRequest.getDroppedId()+" on : "+dropRequest.getTargetId());
		}
		return null;
	}

}
