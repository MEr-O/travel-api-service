package software.design.travel.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "placeBook")
public class PlaceBook {
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
    private int ticket;
    private String date;
}
