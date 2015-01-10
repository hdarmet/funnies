package com.ithaque.funnies.shared.basic.items;

public class StatusItem extends SpriteImageItem {

	int status = 0;
	
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

}
