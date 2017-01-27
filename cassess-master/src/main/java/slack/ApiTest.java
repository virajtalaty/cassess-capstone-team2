package slack;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTest {
	
	
	public ApiTest () {
		
	}
	
	private boolean ok;

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

}
