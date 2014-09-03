package org.utils;

public enum PeligroSource {
	MOBILE(1), WEBPAGE(2);
	private int value;

	private PeligroSource(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
