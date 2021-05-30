package software.design.travel.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponse {
	private String message;
	private boolean status;

	public MessageResponse(String message) {
	    this.message = message;
	  }
}
