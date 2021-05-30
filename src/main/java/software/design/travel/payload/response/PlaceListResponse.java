package software.design.travel.payload.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import software.design.travel.model.Place;

import java.util.List;

@Setter
@Getter
public class PlaceListResponse {
	private List<Place> places;
	private boolean status;
	private String message;
}
