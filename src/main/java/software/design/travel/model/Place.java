package software.design.travel.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import software.design.travel.model.enumType.ERole;

@Setter
@Getter
@Document(collection = "place")
public class Place {
    @Id
    private String id;

    private Image img;
    private String name;
    private String location;
    private String tag;
    private double rating;
    private String description;
    private double fullprice;
    private double currentprice;
    private boolean popular;
    private boolean suggest;
}
