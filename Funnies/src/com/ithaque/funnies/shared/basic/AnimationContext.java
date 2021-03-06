package com.ithaque.funnies.shared.basic;

import com.ithaque.funnies.shared.Location;

public interface AnimationContext {
	
	
//	public static class Key {
//
//		String label;
//
//		public Key(String label) {
//			this.label = label;
//		}
//		
//		@Override
//		public String toString() {
//			return label;
//		}
//		
//	}

//	Location getLocation(Key locationKey);
//	
//	Moveable getItem(Key itemKey);
//	
//	Float getFactor(Key factorKey);
	
	public interface LocationFinder {
		Location find(AnimationContext context);
	}
	
	public interface MoveableFinder {
		Moveable find(AnimationContext context);
	}
	
	public interface FactorFinder {
		Float find(AnimationContext context);
	}
}
