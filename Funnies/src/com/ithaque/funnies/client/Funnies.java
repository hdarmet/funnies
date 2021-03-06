package com.ithaque.funnies.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ithaque.funnies.client.platform.gwt.impl.GWTPlatform;
import com.ithaque.funnies.client.platform.gwt.test.GWTTestPlatform;
import com.ithaque.funnies.shared.Location;
import com.ithaque.funnies.shared.basic.Board;
import com.ithaque.funnies.shared.basic.Color;
import com.ithaque.funnies.shared.basic.Event;
import com.ithaque.funnies.shared.basic.Event.Type;
import com.ithaque.funnies.shared.basic.Font;
import com.ithaque.funnies.shared.basic.Layer;
import com.ithaque.funnies.shared.basic.MultiLayered;
import com.ithaque.funnies.shared.basic.Processor;
import com.ithaque.funnies.shared.basic.items.ImageItem;
import com.ithaque.funnies.shared.basic.items.PolygonItem;
import com.ithaque.funnies.shared.basic.items.TextItem;
import com.ithaque.funnies.shared.basic.items.animations.DropAnimation;
import com.ithaque.funnies.shared.basic.items.animations.FaceFadingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.MoveAnimation;
import com.ithaque.funnies.shared.basic.items.animations.RotateAnimation;
import com.ithaque.funnies.shared.basic.items.animations.ScalingAnimation;
import com.ithaque.funnies.shared.basic.items.animations.easing.LinearEasing;
import com.ithaque.funnies.shared.basic.items.animations.easing.OutBackEasing;
import com.ithaque.funnies.shared.basic.processors.AbstractDragProfile;
import com.ithaque.funnies.shared.basic.processors.AbstractTargetedDragProfile;
import com.ithaque.funnies.shared.basic.processors.DragProcessor;
import com.ithaque.funnies.shared.basic.processors.GestureEvent;
import com.ithaque.funnies.shared.basic.processors.GestureProfile;
import com.ithaque.funnies.shared.basic.processors.GestureRecognition.Gesture;
import com.ithaque.funnies.shared.basic.processors.GestureRecognition.MatchHandler;
import com.ithaque.funnies.shared.basic.processors.ScalingProcessor;
import com.ithaque.funnies.shared.basic.processors.ScrollProfile;
import com.ithaque.funnies.shared.basic.processors.SimpleTargetedDragProfile;
import com.ithaque.funnies.shared.basic.processors.TargetedRotateProfile;
import com.ithaque.funnies.shared.funny.boardgame.GameBoardCircus;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Funnies implements EntryPoint {

	private final FunnyServiceAsync greetingService = GWT
			.create(FunnyService.class);

	public void onModuleLoad() {
		GameBoardCircus gbc = new GameBoardCircus(new GWTPlatform(), 1000.0f, 500.0f);
		FunnyManager funnyManager = new FunnyManager(gbc);
		gbc.init(funnyManager);
		funnyManager.init();
	}	
	
	Board board;
	
	public void onxModuleLoad() {
		board = new Board(new GWTPlatform());
		board.start();
		
		Layer layer = new Layer("layer", -500, -300, 500, 300);
		board.addItem(layer);

		ImageItem item = new ImageItem("hexagon.png");
		layer.addItem(item);

		MoveAnimation animation = new MoveAnimation(new LinearEasing(1000));
		animation.setLocation(new Location(100, 100));
		animation.setItem(item);
		board.launchAnimation(animation);

/*
		iitem.addEventType(Type.MOUSE_CLICK);
		iitem.addEventType(Type.MOUSE_DOWN);

		MultiLayered layered = new MultiLayered(-500, -300, 500, 300);
		Layer layerTwo = layered.createAttachedLayer("unit");
		Layer dragLayer = layered.createAttachedLayer("drag");
		
		layerTwo.addItem(iitem);
		board.addItem(layered);
		DragProcessor dragProcessor = new DragProcessor();
		SimpleTargetedDragProfile profile = new SimpleTargetedDragProfile(board);
		profile.setDragLayer(dragLayer);
		profile.addDraggeable(iitem);

		dragProcessor.addDragProfile(profile);
		board.addProcessor(dragProcessor);
*/
	}
	
	public void onvModuleLoad() {
		board = new Board(new GWTPlatform());
		board.start();

		ImageItem sitem = new ImageItem("rhexagon.png");
		sitem.setRotation(0.0f);
		sitem.setScale(1.0f);
		sitem.setLocation(100, 0);
		
		ImageItem sitem2 = new ImageItem("rhexagon.png");
		sitem2.setRotation(0.0f);
		sitem2.setScale(1.0f);
		sitem2.setLocation(0, -50);
		
		ImageItem titem = new ImageItem("hhexagon.png","h2exagon.png");
		titem.setOpacity("hhexagon.png", 0.0f);
		titem.setOpacity("h2exagon.png", 0.0f);
		titem.setRotation(0.0f);
		titem.setScale(1.0f);
		titem.setLocation(100, 0);
		titem.setShape(-25, 0, -12, -22, 12, -22, 25, 0, 12, 22, -12, 22);

		ImageItem titem2 = new ImageItem("hhexagon.png","h2exagon.png");
		titem2.setOpacity("hhexagon.png", 0.0f);
		titem2.setOpacity("h2exagon.png", 0.0f);
		titem2.setRotation(0.0f);
		titem2.setScale(1.0f);
		titem2.setLocation(0, -50);
		titem2.setShape(-25, 0, -12, -22, 12, -22, 25, 0, 12, 22, -12, 22);
		
		ImageItem iitem = new ImageItem("hexagon.png");
		iitem.setRotation((float)(Math.PI/24.0f));
		iitem.setScale(0.5f);
		iitem.addEventType(Type.MOUSE_CLICK);
		iitem.addEventType(Type.MOUSE_DOWN);

		ImageItem iitem2 = new ImageItem("hexagon.png");
		iitem2.setRotation(0.0f);
		iitem2.setScale(0.5f);
		iitem2.setShape(-25, 0, -12, -22, 12, -22, 25, 0, 12, 22, -12, 22);
		iitem2.addEventType(Type.MOUSE_CLICK);
		iitem2.addEventType(Type.MOUSE_DOWN);

		PolygonItem pitem = new PolygonItem(new Color(255, 0, 0), new Color(0, 255, 0), 4.0f, 0.5f, 0, 0, 25, -50, 25, 50);
		pitem.setRotation(((float)(Math.PI/6.0)));
		pitem.setScale(0.5f);
		
		TextItem mitem = new TextItem("Hello\nWorld !", new Color(0, 255, 255), new Font("arial", 14, 6));
		mitem.setRotation(((float)(Math.PI/6.0)));
		mitem.setLocation(50, 50);
		
		MultiLayered layered = new MultiLayered(-500, -300, 500, 300);
		Layer layerZero = layered.createAttachedLayer("back");
		Layer gestureLayer = layered.createAttachedLayer("gesture");
		Layer layerOne = layered.createAttachedLayer("map");
		Layer layerTwo = layered.createAttachedLayer("unit");
		Layer dragLayer = layered.createAttachedLayer("drag");
		board.addItem(layered);

		layerZero.addItem(sitem);
		layerZero.addItem(sitem2);
		layerOne.addItem(titem);
		layerOne.addItem(titem2);
		layerTwo.addItem(iitem);
		layerTwo.addItem(iitem2);
		layerTwo.addItem(pitem);
		layerTwo.addItem(mitem);
		layerOne.addEventType(Type.MOUSE_DOWN);
		layerOne.addEventType(Type.MOUSE_WHEEL);
		gestureLayer.addEventType(Type.MOUSE_DOWN);
		
		layerOne.setScale(2.0f);
		layerOne.setRotation((float)Math.PI);
		layerOne.setLocation(new Location(-50.0f, 15.0f));

		DragProcessor dragProcessor = new DragProcessor();
		SimpleTargetedDragProfile profile = new SimpleTargetedDragProfile(board);
		profile.setDragLayer(dragLayer);
		profile.addDraggeable(iitem);
		profile.addTarget(titem);
		profile.addTarget(titem2);
		profile.setBeginDragAnimation(new ScalingAnimation.Builder(500, 0.6f).setItem(AbstractDragProfile.draggedItem()));
//		profile.setAdjustLocationAnimation(new ScalingAnimation.Builder(500, 0.5f));
		profile.setAdjustLocationAnimation(new DropAnimation.Builder(500).setItem(AbstractDragProfile.draggedItem()).setLocation(AbstractDragProfile.dropLocation()));
		profile.setEnterTargetAnimation(new FaceFadingAnimation.Builder(1000, 0, 1.0f).setItem(AbstractTargetedDragProfile.newDropTarget()));
		profile.setExitTargetAnimation(new FaceFadingAnimation.Builder(1000, 0, 0.0f).setItem(AbstractTargetedDragProfile.previousDropTarget()));
		profile.setDraggedDropAnimation(new ScalingAnimation.Builder(500, 0.5f).setItem(AbstractDragProfile.draggedItem()));
		profile.setShowAllowedTargetAnimation(new FaceFadingAnimation.Builder(500, 1, 0.2f).setItem(AbstractTargetedDragProfile.possibleDropTarget()));
		profile.setHideAllowedTargetAnimation(new FaceFadingAnimation.Builder(500, 1, 0.0f).setItem(AbstractTargetedDragProfile.possibleDropTarget()));
		
		TargetedRotateProfile rotateProfile = new TargetedRotateProfile();
		rotateProfile.setFinishRotationAnimation(new RotateAnimation.Builder(1000)
			.setItem(TargetedRotateProfile.rotatableItem())
			.setRotation(TargetedRotateProfile.rotation()));
		rotateProfile.addRotatable(iitem2);
		
		MatchHandler dMatch = new MatchHandler() {
			@Override
			public int matches(Gesture gesture) {
				float py = (gesture.getLastPoint().getY()-gesture.getMinY())/(gesture.getMaxY()-gesture.getMinY());
				return py>0.8 ? gesture.getCost() : 10000;
			}
		};
		MatchHandler qMatch = new MatchHandler() {
			@Override
			public int matches(Gesture gesture) {
				float py = (gesture.getLastPoint().getY()-gesture.getMinY())/(gesture.getMaxY()-gesture.getMinY());
				return py<0.3 ? gesture.getCost() : 10000;
			}
		};
		MatchHandler gMatch = new MatchHandler() {
			@Override
			public int matches(Gesture gesture) {
				float py = (gesture.getLastPoint().getY()-gesture.getMinY())/(gesture.getMaxY()-gesture.getMinY());
				return py>0.3 ? gesture.getCost() : 10000;
			}
		};
		MatchHandler pMatch = new MatchHandler() {
			@Override
			public int matches(Gesture gesture) {
				float py = (gesture.getLastPoint().getY()-gesture.getMinY())/(gesture.getMaxY()-gesture.getMinY());
				return py<0.7 ? gesture.getCost() : 10000;
			}
		};
		MatchHandler oMatch = new MatchHandler() {
			@Override
			public int matches(Gesture gesture) {
				float py = (gesture.getLastPoint().getY()-gesture.getMinY())/(gesture.getMaxY()-gesture.getMinY());
				return py<0.3 ? gesture.getCost() : 10000;
			}
		};
		MatchHandler sixMatch = new MatchHandler() {
			@Override
			public int matches(Gesture gesture) {
				float py = (gesture.getLastPoint().getY()-gesture.getMinY())/(gesture.getMaxY()-gesture.getMinY());
				return py>0.3 ? gesture.getCost() : 10000;
			}
		};
		MatchHandler fiveMatch = new MatchHandler() {
			@Override
			public int matches(Gesture gesture) {
				int pos = gesture.indexOf(1, 1, 1);
				return pos==-1 ? gesture.getCost() : 10000;
			} 
		};	
		
		
		GestureProfile gestureProfile = new GestureProfile(gestureLayer);
		gestureProfile.addGesture("A", null, 7, 1);
		gestureProfile.addGesture("B", null, 2, 6, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4);
		gestureProfile.addGesture("C", null, 4, 3, 2, 1, 0);
		gestureProfile.addGesture("D", dMatch, 2, 6, 7, 0, 1, 2, 3, 4);
		gestureProfile.addGesture("E", null, 4, 3, 2, 1, 0, 4, 3, 2, 1, 0);
		gestureProfile.addGesture("F", null, 4, 2);
		gestureProfile.addGesture("G", gMatch, 4, 3, 2, 1, 0, 7, 6, 5, 0);
		gestureProfile.addGesture("H", null, 2, 6, 7, 0, 1, 2);
		gestureProfile.addGesture("I", null, 2);
		gestureProfile.addGesture("J", null, 2, 3, 4);
		gestureProfile.addGesture("K", null, 3, 4, 5, 6, 7, 0, 1);
		gestureProfile.addGesture("L", null, 2, 0);
		gestureProfile.addGesture("M", null, 6, 1, 7, 2);
		gestureProfile.addGesture("N", null, 6, 1, 6);
		gestureProfile.addGesture("O", oMatch, 4, 3, 2, 1, 0, 7, 6, 5, 4);
		gestureProfile.addGesture("P", pMatch, 2, 6, 7, 0, 1, 2, 3, 4);
		gestureProfile.addGesture("Q", qMatch, 4, 3, 2, 1, 0, 7, 6, 5, 4, 0);
		gestureProfile.addGesture("R", null, 2, 6, 7, 0, 1, 2, 3, 4, 1);
		gestureProfile.addGesture("S", null, 4, 3, 2, 1, 0, 1, 2, 3, 4);
		gestureProfile.addGesture("T", null, 0, 2);
		gestureProfile.addGesture("U", null, 2, 1, 0, 7, 6);
		gestureProfile.addGesture("V", null, 1, 7);
		gestureProfile.addGesture("W", null, 2, 7, 1, 6);
		gestureProfile.addGesture("X", null, 1, 0, 7, 6, 5, 4, 3);
		gestureProfile.addGesture("Y", null, 2, 1, 0, 7, 6, 2, 3, 4, 5, 6, 7);
		gestureProfile.addGesture("Z", null, 0, 3, 0);
		
		gestureProfile.addGesture("0", null, 4, 3, 2, 1, 0, 7, 6, 5, 4, 2);
		gestureProfile.addGesture("1", null, 2);
		gestureProfile.addGesture("2", null, 7, 0, 1, 2, 3, 0);
		gestureProfile.addGesture("3", null, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4);
		gestureProfile.addGesture("4", null, 3, 0, 2);
		gestureProfile.addGesture("5", fiveMatch, 4, 2, 0, 1, 2, 3, 4);
		gestureProfile.addGesture("6", sixMatch, 4, 3, 2, 1, 0, 7, 6, 5);
		gestureProfile.addGesture("7", null, 0, 3);
		gestureProfile.addGesture("8", null, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4);
		gestureProfile.addGesture("9", null, 4, 3, 2, 1, 0, 7, 6, 2);

		//dragProcessor.addDragProfile(gestureProfile);
		dragProcessor.addDragProfile(profile);
		dragProcessor.addDragProfile(rotateProfile);
		
		ScrollProfile scroll = new ScrollProfile(layerOne); 
		dragProcessor.addDragProfile(scroll);

		ScalingProcessor scalingProcessor = new ScalingProcessor(1.1f, 0.25f, 3.0f);
		scalingProcessor.addScalable(layerOne);
		
		board.addProcessor(dragProcessor);
		board.addProcessor(scalingProcessor);

		
//		board.addProcessor(new Processor() {
//			public boolean process(Event event, Board board) {
//				System.out.println("I:"+board.getTarget(event));
//				return false;
//			}
//		});
		
		board.addProcessor(new Processor() {

			@Override
			public boolean process(Event event, Board board) { 
				if (event instanceof GestureEvent) {
					System.out.println("Typed : "+((GestureEvent)event).getGesture().getMatch().getId()+" "+((GestureEvent)event).getGesture().getCost());
				}
				return false;
			}
			
		});
		
		MoveAnimation animation = new MoveAnimation(new OutBackEasing(5000));
		animation.setLocation(new Location(100, 100));
		animation.setItem(iitem2);
		ScalingAnimation scalingAnimation = new ScalingAnimation(new OutBackEasing(5000), 2.0f);
		scalingAnimation.setItem(iitem2);
		RotateAnimation rotateAnimation = new RotateAnimation(new OutBackEasing(5000), 2.0f);
		rotateAnimation.setItem(iitem2);
		board.launchAnimation(animation);
		board.launchAnimation(scalingAnimation);
		board.launchAnimation(rotateAnimation);
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onMModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText("");
				serverResponseLabel.setText("");
				greetingService.invoke(null,
						new AsyncCallback<Response>() {
							public void onFailure(Throwable caught) {
								
							}

							public void onSuccess(Response result) {
								
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
