package software.design.travel.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import software.design.travel.model.Place;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class PlaceRequest {
	private MultipartFile multipartFilesDocument;
	private Place place;
}
