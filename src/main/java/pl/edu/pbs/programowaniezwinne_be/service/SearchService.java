package pl.edu.pbs.programowaniezwinne_be.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.model.elastic.ProjectIdx;

public interface SearchService {
    void indexProject(Project project);
    Page<ProjectIdx> searchByName(String name, Pageable pageable);
}
