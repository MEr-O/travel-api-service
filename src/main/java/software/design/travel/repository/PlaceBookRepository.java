package software.design.travel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import software.design.travel.model.Place;
import software.design.travel.model.PlaceBook;

import java.util.List;

public interface PlaceBookRepository extends MongoRepository<PlaceBook, String> {
    Place findPlaceById(String id);

    long countByNameLike(String name);

    List<Place> findByNameLike(String name);
    List<Place> findByTagLike(String name);
}