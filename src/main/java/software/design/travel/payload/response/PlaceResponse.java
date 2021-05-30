package software.design.travel.payload.response;

import lombok.Getter;
import lombok.Setter;
import software.design.travel.model.Place;

import java.util.List;

@Setter
@Getter
public class PlaceResponse {
	private List<Place> places;
	private long total;
	private boolean status;
	private String message;
}
