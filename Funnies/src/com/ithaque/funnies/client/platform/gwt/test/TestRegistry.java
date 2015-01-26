package com.ithaque.funnies.client.platform.gwt.test;

import java.util.ArrayList;
import java.util.List;

public class TestRegistry {

	static long count;
	
	static List<TestRecord> records = new ArrayList<TestRecord>();
	
	public static class TestRecord {
		
		public TestRecord(long rank, String clazz, String id, String method,
				String[] parameters) {
			super();
			this.rank = rank;
			this.clazz = clazz;
			this.id = id;
			this.method = method;
			this.parameters = parameters;
		}
		
		long rank;
		String clazz;
		String id;
		String method;
		String[] parameters;
		
		@Override
		public String toString() {
			StringBuilder params = new StringBuilder();
			if (parameters.length>0) {
				params.append(parameters[0]);
				for (int index=1; index<parameters.length; index++) {
					params.append(", ");
					params.append(parameters[index]);
				}
			}
			return rank+":"+clazz+"["+id+"]."+method+"("+params+")";
		}
		
	}
	
	public static long getRank() {
		return count++;
	}
	
	public static void addCall(String clazz, String id, String method, String ... params) {
		records.add(new TestRecord(getRank(), clazz, id, method, params));
	}
	
	public static void reset() {
		records.clear();
		count = 0;
	}
	
	public static void dump() {
		for (TestRecord record : records) {
			System.out.println(record);
		}
	}
}
