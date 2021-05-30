package software.design.travel.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class Login2FactorRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String code;
}
