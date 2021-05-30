package software.design.travel.payload.response;

import lombok.Getter;
import lombok.Setter;
import software.design.travel.model.Place;

import java.util.List;

@Setter
@Getter
public class PlaceDetailResponse {
	private boolean status;
	private String message;
	private Place place;
}
