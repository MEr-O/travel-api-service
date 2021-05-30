package software.design.travel.payload.response;

import lombok.Getter;
import lombok.Setter;
import software.design.travel.model.Place;
import software.design.travel.model.PlaceBook;
import software.design.travel.model.Role;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class UserResponse {
	private boolean status;
	private String message;
	private User user;

	@Setter
	@Getter
	public static class User {
		private String username;
		private String email;
		private List<PlaceBook> places;
		private Set<Role> roles;
		private String firstname;
		private String lastname;
		private String mobileNumber;
		private String gender;
	}
}
