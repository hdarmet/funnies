package com.ithaque.funnies.server;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import com.ithaque.funnies.client.platform.gwt.Context2D;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.shared.IllegalInvokeException;
import com.ithaque.funnies.shared.basic.Color;

public class Java2DContext2D implements Context2D {

	Java2DCanvas canvas;
	Graphics2D context = null;
	VolatileImage image = null;
	float lineWidth = 1.0f;
	int lineJoin = BasicStroke.JOIN_ROUND;
	GeneralPath path = new GeneralPath();;
	java.awt.Color strokeColor = java.awt.Color.BLACK;
	java.awt.Color fillColor = java.awt.Color.WHITE;
	String baseline = "top";
	
	public Java2DContext2D(Java2DCanvas canvas) {
		this.canvas = canvas;
	}
	
	protected Graphics2D getContext() {
		if (canvas.getImage()!=image) {
			image = canvas.getImage();
			context = image.createGraphics();
			context.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		return context;
	}
	
	@Override
	public void setTransform(float m11, float m12, float m21, float m22, float dx, float dy) {
		getContext().setTransform(new AffineTransform(m11, m12, m21, m22, dx, dy));
	}

	@Override
	public void setGlobalAlpha(float opacity) {
		getContext().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
	}

	@Override
	public void translate(float x, float y) {
		AffineTransform transform = getContext().getTransform();
		transform.translate(x, y);
		getContext().setTransform(transform);
	}

	@Override
	public void drawImage(ImageInterface image, 
			int sx, int sy, int sw, int sh,
			int dx, int dy, int dw, int dh) 
	{
		Image implImage = ((Java2DImage)image).getImage();
		getContext().drawImage(implImage, dx, dy, dx+dw, dx+dh, sx, sy, sx+sw, sy+sh, null);
	}

	@Override
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	@Override
	public void setLineJoin(String lineJoin) {
		if (lineJoin.equals("miter")) {
			this.lineJoin = BasicStroke.JOIN_MITER;
		}
		else if (lineJoin.equals("round")) {
			this.lineJoin = BasicStroke.JOIN_ROUND;
		}
		else if (lineJoin.equals("bevel")) {
			this.lineJoin = BasicStroke.JOIN_BEVEL;
		}
		else {
			throw new IllegalInvokeException();
		}
	}

	@Override
	public void setStrokeStyle(Color color) {
		strokeColor = new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void setFillStyle(Color color) {
		fillColor = new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void fill() {
		getContext().setPaint(fillColor);
		getContext().fill(path);
	}

	@Override
	public void stroke() {
		BasicStroke stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, lineJoin);
		getContext().setStroke(stroke);
		getContext().setPaint(strokeColor);
		getContext().draw(path);
	}

	@Override
	public void beginPath() {
		path = new GeneralPath();
	}

	@Override
	public void moveTo(float x, float y) {
		path.moveTo(x, y);
	}

	@Override
	public void lineTo(float x, float y) {
		path.lineTo(x, y);
	}

	@Override
	public void clip() {
		context.clip(path);
	}

	@Override
	public void setFont(String font) {
		getContext().setFont(new Font(font, Font.PLAIN, 12));
	}

	@Override
	public void setTextBaseline(String baseline) {
		this.baseline = baseline;
	}

	@Override
	public void fillText(String text, float x, float y) {
		int delta = 0;
		if (baseline.equals("top")) {
			delta = context.getFontMetrics(context.getFont()).getMaxAscent();
		}
		getContext().drawString(text, (int)x, (int)y+delta);
	}

	@Override
	public double measureTextWidth(String text) {
		return getContext().getFontMetrics(context.getFont()).stringWidth(text);
	}

	@Override
	public void clearRect(final float x, final float y, final float w, final float h) {
		getContext().setBackground(new java.awt.Color(0, 0, 0, 0));
		getContext().clearRect((int)x, (int)y, (int)w, (int)h);
	}

	public void renew() {
		if (canvas.getImage()!=image) {
			image = canvas.getImage();
		}
		context = image.createGraphics();
		context.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

}
