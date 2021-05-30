package software.design.travel.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import software.design.travel.model.Code;
import software.design.travel.model.User;

import java.util.Optional;

public interface CodeRepository extends MongoRepository<Code, String> {
    Code findTopByUsernameOrderByIdDesc(String username);
}