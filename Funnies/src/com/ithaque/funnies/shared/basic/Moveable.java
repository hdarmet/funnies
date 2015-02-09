package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.Location;

public interface Moveable {
	float getScale();
	float getRotation();
	float getOpacity();
	float getDisplayOpacity();
	Location getLocation();
	Board getBoard();
}
