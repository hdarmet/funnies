package com.ithaque.funnies.shared.basic;

public class Font {

	String fontName;
	int size;
	int margin;
	
	public Font(String fontName, int size, int margin) {
		this.fontName = fontName;
		this.size = size;
		this.margin = margin;
	}

	public String getFontName() {
		return fontName;
	}

	public int getSize() {
		return size;
	}
	
	public int getMargin() {
		return margin;
	}
}
