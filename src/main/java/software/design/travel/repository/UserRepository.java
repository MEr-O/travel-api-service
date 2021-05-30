package software.design.travel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import software.design.travel.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    User findUserByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}