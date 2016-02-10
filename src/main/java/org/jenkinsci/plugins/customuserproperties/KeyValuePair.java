package org.jenkinsci.plugins.customuserproperties;

/**
 * Class to hold key value Pairs to be used in {@link CustomUserProperties}
 * 
 * @author nabel.sawiris
 */
public class KeyValuePair {
	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
