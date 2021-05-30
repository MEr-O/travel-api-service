package software.design.travel.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import software.design.travel.model.enumType.ERole;

@Setter
@Getter
@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private ERole name;
}
