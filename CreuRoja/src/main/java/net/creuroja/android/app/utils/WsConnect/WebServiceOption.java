package net.creuroja.android.app.utils.WsConnect;

/**
 * Created by lapuente on 30.07.14.
 */
public class WebServiceOption {
	public OptionType optionType;
	public String key;
	public String value;

	public WebServiceOption(OptionType optionType, String key, String value) {
		this.optionType= optionType;
		this.key = key;
		this.value = value;
	}

	public enum OptionType {
		GET, POST, HEADER;
	}
}
