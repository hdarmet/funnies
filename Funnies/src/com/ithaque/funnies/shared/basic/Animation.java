package com.ithaque.funnies.shared.basic;

public interface Animation {

	public static final long INTERVAL = 40;

	boolean animate(long time);

	void finish(long time);

	boolean start(long time);

	AnimationContext getContext();

	void setContext(AnimationContext context);

	long getDuration();

	public interface Factory {
		Animation create();
	}

}
