package software.design.travel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "code")
@Setter
@Getter
@NoArgsConstructor
public class Code {
    @Id
    private String id;

    private String username;

    private String code;
}
