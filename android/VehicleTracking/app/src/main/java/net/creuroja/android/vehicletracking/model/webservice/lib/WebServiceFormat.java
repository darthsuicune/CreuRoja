package net.creuroja.android.vehicletracking.model.webservice.lib;

/**
 * Created by lapuente on 30.07.14.
 */

public enum WebServiceFormat {
	HTML(".html"), JSON(".json");

	private String format;

	private WebServiceFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return format;
	}
}