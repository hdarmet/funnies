package com.ithaque.funnies.shared.basic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemComparator implements Comparator<Item> {

	@Override
	public int compare(Item item1, Item item2) {
		if (item1==item2) {
			return 0;
		}
		if (item1==null) {
			return -1;
		}
		if (item2==null) {
			return 1;
		}
		LayoutDevice item1Layout = item1.getLayout();
		LayoutDevice item2Layout = item2.getLayout();
		if (item1Layout!=item2Layout) {
			if (item1Layout==null) {
				return -1;
			}
			if (item2Layout==null) {
				return 1;
			}
			return Graphics.Singleton.getGraphics().compareLayers(item1Layout.getLayerToken(), item2Layout.getLayerToken());
		}
		if (item1.level!=item2.level) {
			return item1.level-item2.level;
		}
		List<Integer> item1Path = getPath(item1);
		List<Integer> item2Path = getPath(item2);
		return comparePathes(item1Path, item2Path);
	}
	
	List<Integer> getPath(Item item) {
		List<Integer> path = new ArrayList<Integer>();
		buildPath(item, path);
		return path;
	}
	
	void buildPath(Item item, List<Integer> path) {
		while (item.getParent()!=null && !(item.getParent() instanceof LayoutDevice)) {
			path.add(item.getParent().indexOfItem(item));
			item=(Item)item.getParent();
		}
	}	

	int comparePathes(List<Integer> left, List<Integer> right) {
		if (left==null || right==null) {
			if (left!=null) {
				return 1;
			}
			if (right!=null) {
				return -1;
			}
			return 0;
		}
		int length = left.size() > right.size() ? right.size() : left.size();
		for (int index=0; index<length; index++) {
			int result = right.get(index).compareTo(left.get(index));
			if (result!=0) {
				return result;
			}
		}
		if (left.size()>length) {
			return -1;
		}
		if (right.size()>length) {
			return 1;
		}
		return 0;
	}

}
