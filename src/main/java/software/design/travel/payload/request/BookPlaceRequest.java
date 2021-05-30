package software.design.travel.payload.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookPlaceRequest {
    private String placeId;
    private String username;
    private int ticket;
    private String date;
}
