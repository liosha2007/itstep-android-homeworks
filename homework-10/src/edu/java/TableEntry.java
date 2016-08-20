package edu.java;

public class TableEntry {
	public final String name;
	public final String value;
	
	public TableEntry(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
