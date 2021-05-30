package software.design.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import software.design.travel.repository.FileStorageRepository;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageRepository.class
})
public class TravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelApplication.class, args);
	}

}
