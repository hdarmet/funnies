package com.ithaque.funnies.shared.basic.items;

import com.ithaque.funnies.shared.basic.Animation;
import com.ithaque.funnies.shared.basic.AnimationContext;
import com.ithaque.funnies.shared.basic.Board;

public class StatusItem extends SpriteImageItem {

	public static final int DEFAULT_SPEED = 40;
	
	int status = 0;
	int changeSpeed = DEFAULT_SPEED;
	ChangeStatusAnimation currentStatusAnimation = null;
	
	public StatusItem(String url, int status, ImageConfiguration ... configurations) {
		super(url, configurations);
		this.status = status;
		initOpacities(status);
	}

	public StatusItem(String url, int status, 
			int colCount, int rowCount, 
			float imageWidth, float imageHeight) {
		super(url, colCount, rowCount, imageWidth, imageHeight);
		this.status = status;
		initOpacities(status);
	}

	void initOpacities(int status) {
		for (int index=0; index<getImageCount(); index++) {
			setOpacity(index, index==status?1.0f:0.0f);
		}
	}

	public void changeStatus(int status) {
		Board board = getBoard();
		if (board==null) {
			this.status = status;
		}
		else {
			if (currentStatusAnimation!=null) {
				currentStatusAnimation.canceled = true;
			}
			currentStatusAnimation = new ChangeStatusAnimation(status);
			board.launchAnimation(currentStatusAnimation);
		}
	}
	
	void setStatus(int status) {
		setOpacity(this.status, 0.0f);
		System.out.println("change status "+this.status+" "+status);
		this.status = status;
		setOpacity(this.status, 1.0f);
	}
	
	class ChangeStatusAnimation implements Animation {

		ChangeStatusAnimation(int targetStatus) {
			this.targetStatus = targetStatus;
		}
		
		long startTime;
		int startStatus;
		int targetStatus;
		int speed;
		boolean canceled = false;

		@Override
		public boolean start(long time) {
			this.startStatus = status;
			this.startTime = time;
			this.speed = changeSpeed;
			return true;
		}

		@Override
		public boolean animate(long time) {
			long duration = getDuration();
			if (canceled || startStatus==targetStatus || time>=startTime+duration) {
				return false;
			}
			else {
				int range = targetStatus-startStatus;
				int delta = (int)(range*(time-startTime)/duration);
				setStatus(startStatus+delta);
				return true;
			}
		}

		@Override
		public void finish(long time) {
			if (!canceled) {
				setStatus(targetStatus);
			}
			if (currentStatusAnimation==this) {
				currentStatusAnimation = null;
			}
		}

		@Override
		public AnimationContext getContext() {
			return null;
		}

		@Override
		public void setContext(AnimationContext context) {
		}

		@Override
		public long getDuration() {
			int change = targetStatus-startStatus;
			return speed*(change>0?change:-change);
		}
		
	}
}
