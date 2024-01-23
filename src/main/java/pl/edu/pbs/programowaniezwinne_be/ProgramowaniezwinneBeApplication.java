package pl.edu.pbs.programowaniezwinne_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "pl.edu.pbs.programowaniezwinne_be.repository")
@EnableElasticsearchRepositories(basePackages = "pl.edu.pbs.programowaniezwinne_be.repository.elastic")
public class ProgramowaniezwinneBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramowaniezwinneBeApplication.class, args);
    }

}
