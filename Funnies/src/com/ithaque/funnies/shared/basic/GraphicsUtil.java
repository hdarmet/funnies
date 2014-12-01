package com.ithaque.funnies.shared.basic;

public class GraphicsUtil {
	
	public static Location inDisplayLimits(
		float containerLeft, float containerUpper, float containerRight, float containerBottom,
		float contentLeft, float contentUpper, float contentRight, float contentBottom) {
		float dX = 0.0f;
		float dY = 0.0f;
		boolean outside = false;
		if (contentLeft<containerLeft) {
			dX = contentLeft-containerLeft;
			outside = true;
		}
		else {
			if (contentRight>containerRight) {
				dX = contentRight-containerRight;
				outside = true;
			}
		}
		if (contentUpper<containerUpper) {
			dY = contentUpper-containerUpper;
			outside = true;
		}
		else {
			if (contentBottom>containerBottom) {
				dY = contentBottom-containerBottom;
				outside = true;
			}
		}
		if (outside) {
			return new Location(dX, dY);
		}
		else {
			return null;
		}
	}
}

