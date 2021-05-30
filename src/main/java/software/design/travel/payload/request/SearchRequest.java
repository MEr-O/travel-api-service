package software.design.travel.payload.request;

import lombok.Getter;
import lombok.Setter;
import software.design.travel.model.enumType.ETag;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
public class SearchRequest {
    private String q;
    private ETag type;
}
