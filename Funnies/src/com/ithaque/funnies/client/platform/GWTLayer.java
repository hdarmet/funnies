package com.ithaque.funnies.client.platform;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.RootPanel;

public class GWTLayer {
	Canvas canvas;
	Canvas flipCanvas;
	Canvas flopCanvas;
	
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
	    canvas.getContext2d().save();
	    RootPanel.get("board").add(canvas);
		return canvas;
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
			flipCanvas.setVisible(true);
			flopCanvas.setVisible(false);
		}
		else {
			canvas = flipCanvas;
			flipCanvas.setVisible(false);
			flopCanvas.setVisible(true);
		}
	    return canvas.getContext2d();
	}

	public void show() {
		if (flipCanvas==canvas) {
			flipCanvas.setVisible(true);
			flopCanvas.setVisible(false);
		}
		else {
			flipCanvas.setVisible(false);
			flopCanvas.setVisible(true);
		}
	}
	
	public Context2d getContext() {
	    return canvas.getContext2d();
	}
}
