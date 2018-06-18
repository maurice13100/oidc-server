package org.mitre.openid.connect.util;

public enum AcrEnum {
	SMS("LOA_2"),
	PWD("LOA_1");

	private String value;
	AcrEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
