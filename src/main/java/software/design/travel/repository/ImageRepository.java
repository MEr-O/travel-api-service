package software.design.travel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import software.design.travel.model.Image;
import software.design.travel.model.Place;

public interface ImageRepository extends MongoRepository<Image, String> {
}