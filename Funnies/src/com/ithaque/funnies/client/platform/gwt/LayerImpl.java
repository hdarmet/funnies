package com.ithaque.funnies.client.platform.gwt;

public class LayerImpl {
	CanvasInterface canvas;
	CanvasInterface flipCanvas;
	CanvasInterface flopCanvas;
	
	public LayerImpl(AbstractGWTPlatform platform) {
	    canvas = flipCanvas = platform.createCanvas(true);
	    flopCanvas = platform.createCanvas(false);
	}
	
	public float getDisplayWidth() {
		return canvas.getCoordinateSpaceWidth();
	}

	public float getDisplayHeight() {
		return canvas.getCoordinateSpaceHeight();
	}

	public void clear() {
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
	
	public Context2D getContext() {
	    return canvas.getContext2d();
	}
	
	public void resetContext() {
		canvas.restoreContext2D();
		canvas.saveContext2D();
	}
}
