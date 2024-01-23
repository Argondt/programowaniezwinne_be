package pl.edu.pbs.programowaniezwinne_be.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.model.elastic.ProjectIdx;
import pl.edu.pbs.programowaniezwinne_be.repository.elastic.ProjectIdxRepository;

@Service
@AllArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final ProjectIdxRepository projectIdxRepository;


    @Override
    public void indexProject(Project project) {
        if (project == null) {
            log.error("Próba indeksacji projektu zakończona niepowodzeniem: projekt jest null.");
            return;
        }

        ProjectIdx projectIdx = toProjectIdx(project);
        if (projectIdx == null) {
            log.error("Próba indeksacji projektu zakończona niepowodzeniem: konwersja na ProjectIdx zwróciła null.");
            return;
        }

        log.info("Indeksacja projektu: {}", project.getId());
        projectIdxRepository.save(projectIdx);
    }

    @Override
    public Page<ProjectIdx> searchByName(String name, Pageable pageable) {
        if (pageable == null) {
            log.error("Pageable cannot be null");
            throw new IllegalArgumentException("Pageable cannot be null");
        }

        if (name == null || name.trim().isEmpty()) {
            log.info("Name is blank, returning all projects");
            return projectIdxRepository.findAll(pageable);
        }

        String trimmedName = name.trim();
        log.info("Searching for projects with name: {}", trimmedName);
        return projectIdxRepository.findAllByName(trimmedName, pageable);

    }

    private static ProjectIdx toProjectIdx(Project project) {
        if (project == null) {
            log.error("Przekazany projekt jest null.");
            return null;
        }
        ProjectIdx projectIdx = new ProjectIdx();
        projectIdx.setProjectId(project.getId());
        projectIdx.setName(project.getDescription());
        projectIdx.setDescription(project.getDescription());

        log.info("Projekt został pomyślnie zamapowany na ProjectIdx.");

        return projectIdx;
    }
}
