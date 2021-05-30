package software.design.travel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import software.design.travel.model.Role;
import software.design.travel.model.User;
import software.design.travel.model.enumType.ERole;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}