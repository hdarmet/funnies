package com.ithaque.funnies.shared.funny;

public interface Funny {

	String getId();
	
	void enterRing(Ring ring);

	public interface Factory {
		Funny createFunny(Param ... params);
		
		public class Param {
			String key;
			String stringValue;
			Float floatValue;
			
			public Param(String key, String stringValue) {
				super();
				this.key = key;
				this.stringValue = stringValue;
			}
			
			public Param(String key, Float floatValue) {
				super();
				this.key = key;
				this.floatValue = floatValue;
			}

			public String getKey() {
				return key;
			}
			public String getStringValue() {
				return stringValue;
			}
			public Float getFloatValue() {
				return floatValue;
			}
			
			public static String getString(Param[] params, String key) {
				for (Param param : params) {
					if (param.key.equals(key)) {
						return param.getStringValue();
					}
				}
				return null;
			}
			
			public static Float getFloat(Param[] params, String key) {
				for (Param param : params) {
					if (param.key.equals(key)) {
						return param.getFloatValue();
					}
				}
				return null;			
			}
			
		}
	}
	
}
