package com.ithaque.funnies.client.platform;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.RootPanel;

public class GWTLayer {
	Canvas canvas;
	Canvas flipCanvas;
	Canvas flopCanvas;
	Context2d context2d;
	
	public GWTLayer(GWTGraphics graphics) {
	    canvas = flipCanvas = createCanvas(graphics, true);
	    flopCanvas = createCanvas(graphics, false);
	}
	
	Canvas createCanvas(GWTGraphics graphics, boolean visible) {
	    Canvas canvas = Canvas.createIfSupported();
	    canvas.getElement().setAttribute("style", "position:absolute;left:0px;top:0px;");
	    canvas.setVisible(visible);
	    canvas.setCoordinateSpaceWidth(1000);
	    canvas.setCoordinateSpaceHeight(500);
	    RootPanel.get("board").add(canvas);
		return canvas;
	}
	
	protected float getCenterX() {
		return canvas.getCoordinateSpaceWidth()/2.0f;
	}
	
	protected float getCenterY() {
		return canvas.getCoordinateSpaceHeight()/2.0f;
	}
	
	public float getDisplayWidth() {
		return canvas.getCoordinateSpaceWidth();
	}

	public float getDisplayHeight() {
		return canvas.getCoordinateSpaceHeight();
	}

	public Context2d clear() {
		if (flipCanvas==canvas) {
			canvas = flopCanvas;
			flipCanvas.setVisible(false);
			flopCanvas.setVisible(true);
		}
		else {
			canvas = flipCanvas;
			flipCanvas.setVisible(true);
			flopCanvas.setVisible(false);
		}
	    context2d = canvas.getContext2d();
		return context2d;
	}

}
