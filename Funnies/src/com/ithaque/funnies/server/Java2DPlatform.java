package com.ithaque.funnies.server;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.google.gwt.dom.client.NativeEvent;
import com.ithaque.funnies.client.platform.gwt.AbstractGWTPlatform;
import com.ithaque.funnies.client.platform.gwt.CanvasInterface;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl;
import com.ithaque.funnies.client.platform.gwt.GraphicsImpl.ImageElementRecord;
import com.ithaque.funnies.client.platform.gwt.ImageInterface;
import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Token;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.MouseEvent.Button;

public class Java2DPlatform extends AbstractGWTPlatform {

	Random random = new Random();
	int frameWidth = 100;
	int frameHeight = 100;
	JFrame jframe;
	List<Java2DCanvas> canvasList = new ArrayList<Java2DCanvas>();
	GraphicsConfiguration configuration;
	VolatileImage screen = null;
	
	public Java2DPlatform() {
	};
	
	@SuppressWarnings("serial")
	public synchronized void execute(Runnable runnable) {
		jframe = new JFrame() {
			@Override
			public void paint(Graphics graphics) {
				synchronized(Java2DPlatform.this) {
					if (screen==null) {
						screen = configuration.createCompatibleVolatileImage(frameWidth, frameHeight);					
					}
					Graphics2D screenCtx = (Graphics2D)screen.getGraphics();
					screenCtx.clearRect(0, 0, frameWidth, frameHeight);
					Graphics2D context = (Graphics2D)graphics;
					for (Java2DCanvas canvas : canvasList) {
						if (canvas.visible) {
							screenCtx.drawImage(canvas.getImage(), 0, 0, null);
						}
					}
					context.drawImage(screen, 0, 0, null);
				}
			};
		};
		configuration = jframe.getGraphicsConfiguration();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
		jframe.addMouseListener(mouseListener);
		jframe.addMouseMotionListener(mouseMotionListener);
		jframe.addMouseWheelListener(mouseWheelListener);
		jframe.pack();
		runnable.run();
	}
	
	Button getButton(int nativeButton) {
		switch (nativeButton) {
		case MouseEvent.BUTTON2 : return Button.RIGHT;
		case MouseEvent.BUTTON1 : return Button.LEFT;
		case MouseEvent.BUTTON3 : return Button.MIDDLE;
		default : return Button.NONE;
		}
	}
	
	MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent event) {
			Type type = event.getClickCount()>1 ? Type.MOUSE_DOUBLE_CLICK : Type.MOUSE_CLICK;
			sendEvent(new com.ithaque.funnies.shared.basic.MouseEvent(type, event.getX(), event.getY(), 
				getButton(event.getButton()), 
				(event.getModifiers()&MouseEvent.SHIFT_DOWN_MASK)!=0, 
				(event.getModifiers()&MouseEvent.CTRL_DOWN_MASK)!=0, 
				(event.getModifiers()&MouseEvent.ALT_DOWN_MASK)!=0));
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent event) {
			sendEvent(new com.ithaque.funnies.shared.basic.MouseEvent(Type.MOUSE_DOWN, event.getX(), event.getY(), 
				getButton(event.getButton()), 
				(event.getModifiers()&MouseEvent.SHIFT_DOWN_MASK)!=0, 
				(event.getModifiers()&MouseEvent.CTRL_DOWN_MASK)!=0, 
				(event.getModifiers()&MouseEvent.ALT_DOWN_MASK)!=0));
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			sendEvent(new com.ithaque.funnies.shared.basic.MouseEvent(Type.MOUSE_UP, event.getX(), event.getY(), 
				getButton(event.getButton()), 
				(event.getModifiers()&MouseEvent.SHIFT_DOWN_MASK)!=0, 
				(event.getModifiers()&MouseEvent.CTRL_DOWN_MASK)!=0, 
				(event.getModifiers()&MouseEvent.ALT_DOWN_MASK)!=0));
		}
		
	};
	
	MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent event) {
			sendEvent(new com.ithaque.funnies.shared.basic.MouseEvent(Type.MOUSE_MOVE, event.getX(), event.getY(), 
					getButton(event.getButton()), 
					(event.getModifiers()&MouseEvent.SHIFT_DOWN_MASK)!=0, 
					(event.getModifiers()&MouseEvent.CTRL_DOWN_MASK)!=0, 
					(event.getModifiers()&MouseEvent.ALT_DOWN_MASK)!=0));
		}
		
		@Override
		public void mouseDragged(MouseEvent event) {
			sendEvent(new com.ithaque.funnies.shared.basic.MouseEvent(Type.MOUSE_DRAG, event.getX(), event.getY(), 
					getButton(event.getButton()), 
					(event.getModifiers()&MouseEvent.SHIFT_DOWN_MASK)!=0, 
					(event.getModifiers()&MouseEvent.CTRL_DOWN_MASK)!=0, 
					(event.getModifiers()&MouseEvent.ALT_DOWN_MASK)!=0));
		}
	};
	
	MouseWheelListener mouseWheelListener = new MouseWheelListener() {
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent event) {
			sendEvent(new com.ithaque.funnies.shared.basic.MouseEvent(Type.MOUSE_WHEEL, event.getX(), event.getY(), 
					event.getWheelRotation()>0?Button.WHEEL_SOUTH:Button.WHEEL_NORTH, 
					(event.getModifiers()&MouseEvent.SHIFT_DOWN_MASK)!=0, 
					(event.getModifiers()&MouseEvent.CTRL_DOWN_MASK)!=0, 
					(event.getModifiers()&MouseEvent.ALT_DOWN_MASK)!=0));
		}
	};
	
	@Override
	public long getTime() {
		return System.currentTimeMillis();
	}

	@Override
	public float randomize() {
		return random.nextFloat();
	}

	@Override
	public Token start(final Board board) {
		Token token = super.start(board);
		new Thread(()->{
			for (;;) {
				long time = getTime();
				boolean dirty;
				synchronized(Java2DPlatform.this) {
					dirty = process(time);
				}
				if (dirty) {
					jframe.repaint();
				}
				try{
					Thread.sleep(Animation.INTERVAL);
				}
				catch(InterruptedException ie){
				}
			}
		}).start();
		return token;
	}


	@Override
	public CanvasInterface createCanvas(boolean visible) {
		Java2DCanvas canvas = new Java2DCanvas(this, visible);
	    initCanvas(canvas, visible);
	    return canvas;

	}

	@Override
	public ImageInterface createImage(String url, ImageElementRecord record) {
		return new Java2DImage(configuration, url, (GraphicsImpl)getGraphics(), record);
	}

	public void setFrameWidth(int width) {
		this.frameWidth = width;
		jframe.setSize(frameWidth, frameHeight);
	}

	public void setFrameHeight(int height) {
		this.frameHeight = height;
		jframe.setSize(frameWidth, frameHeight);
	}

	public void addToJFrame(Java2DCanvas canvas) {
		canvasList.add(canvas);
	}

}
