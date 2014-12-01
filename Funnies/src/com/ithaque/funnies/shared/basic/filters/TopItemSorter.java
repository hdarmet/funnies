package com.ithaque.funnies.shared.basic.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ithaque.funnies.shared.basic.Item;
import com.ithaque.funnies.shared.basic.ItemFilter;

public class TopItemSorter extends ItemFilter {

	Comparator<List<Integer>> comparator = new Comparator<List<Integer>>() {

		@Override
		public int compare(List<Integer> left, List<Integer> right) {
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
	};
	
	public TopItemSorter() {
		super();
	}

	public TopItemSorter(ItemFilter filter) {
		super(filter);
	}

	@Override
	public Collection<Item> filter(Collection<Item> items) {
		SortedMap<List<Integer>, Item> sortedItems = new TreeMap<List<Integer>, Item>(comparator);
		for (Item item : items) {
			List<Integer> itemRanges = new ArrayList<Integer>();
			getItemRanges(item, itemRanges);
			sortedItems.put(itemRanges, item);
		}
		return sortedItems.values();
	}

	void getItemRanges(Item item, List<Integer> itemRanges) {
		if (item.getParent()!=null && item.getParent() instanceof Item) {
			getItemRanges((Item)item.getParent(), itemRanges);
		}
		itemRanges.add(item.getParent().indexOfItem(item));
	}
	
}
