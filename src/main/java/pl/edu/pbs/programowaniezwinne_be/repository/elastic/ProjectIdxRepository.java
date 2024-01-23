package pl.edu.pbs.programowaniezwinne_be.repository.elastic;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.stereotype.Repository;
import pl.edu.pbs.programowaniezwinne_be.model.elastic.ProjectIdx;

import java.util.UUID;

@Repository
public interface ProjectIdxRepository extends ElasticsearchRepository<ProjectIdx, String> {
    @Query("{\"bool\": {\"should\": [{\"wildcard\": {\"name\": \"?0\"}}, {\"wildcard\": {\"name\": \"*?0*\"}}, {\"wildcard\": {\"name\": \"*?0\"}}], \"minimum_should_match\": 1}}")
    Page<ProjectIdx> findAllByName(String name, Pageable pageable);

    void deleteProjectIdxByProjectId(UUID projectid);
}
