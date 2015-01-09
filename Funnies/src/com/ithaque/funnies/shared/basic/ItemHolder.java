package com.ithaque.funnies.shared.basic;

public interface ItemHolder extends Moveable {

	public static final float STANDARD_SCALE = 1.0f;
	public static final float NO_ROTATION = 0.0f;
	public static final float FULL_OPACITY = 1.0f;
	public static final float INVISIBLE = 0.0f;
	
	void dirty();
	void addItem(Item item);
	void addItem(int index, Item item);
	void setItem(int index, Item item);
	Item getItem(int index);
	int getItemCount();
	void removeItem(Item item);
	boolean contains(Item item);
	int indexOfItem(Item item);
	float getAbsoluteRotation();
	float getAbsoluteScale();

}
