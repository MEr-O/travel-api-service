package software.design.travel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "image")
@Setter
@Getter
@NoArgsConstructor
public class Image {
    @Id
    private String id;

    private String originName;

    private String imagePath;
}
