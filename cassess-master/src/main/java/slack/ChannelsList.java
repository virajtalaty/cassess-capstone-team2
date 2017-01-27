package slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelsList {

	private boolean ok;
	private ChannelObject[] channels;
	private String warning;
	private String error;
	
	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @param ok the ok to set
	 */
	public void setOk(boolean ok) {
		this.ok = ok;
	}

	/**
	 * @return the channels
	 */
	public ChannelObject[] getChannels() {
		return channels;
	}

	/**
	 * @param channels the channels to set
	 */
	public void setChannels(ChannelObject[] channels) {
		this.channels = channels;
	}

	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
	}

	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	public ChannelsList() {
		
	}
	
}
