package software.design.travel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import software.design.travel.model.Place;

import java.util.List;

public interface PlaceRepository extends MongoRepository<Place, String> {
    Place findPlaceById(String id);
    List<Place> findAllByPopularIsTrue();
    List<Place> findAllBySuggestIsTrue();

    long countByNameLike(String name);

    List<Place> findByNameLike(String name);
    List<Place> findByTagLike(String name);
}