package com.ithaque.funnies.shared.basic;

public interface AnimationContext {
	
	
	public static class Key {

		String label;

		public Key(String label) {
			this.label = label;
		}
		
		@Override
		public String toString() {
			return label;
		}
		
	}

	Location getLocation(Key locationKey);
	
	Item getItem(Key itemKey);
	
	Float getFactor(Key factorKey);
}
