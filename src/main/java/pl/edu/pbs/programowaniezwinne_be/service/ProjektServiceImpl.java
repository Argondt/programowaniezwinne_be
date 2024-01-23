package pl.edu.pbs.programowaniezwinne_be.service;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.repository.ProjektRepository;
import pl.edu.pbs.programowaniezwinne_be.repository.ZadanieRepository;
import pl.edu.pbs.programowaniezwinne_be.repository.elastic.ProjectIdxRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjektServiceImpl implements ProjektService {

    private final ProjektRepository projektRepository;
    private final ZadanieRepository zadanieRepository;
    private final SearchService searchService;
    private final KeycloakService keycloakService;
    private final ProjectIdxRepository elasticsearchRepository;

    @Override
    public Optional<Project> getProjekt(UUID projektId) {
        return projektRepository.findById(projektId);
    }

    @Override
    public Project createProject(ProjektDTO projekt) {
        if (projekt == null) {
            log.error("Attempt to create a project with a null object");
            throw new IllegalArgumentException("Project cannot be null");
        }
        Project projectCreate = new Project();
        projectCreate.setName(projekt.getNazwa());
        projectCreate.setDescription(projekt.getOpis());
        keycloakService.createGroup(projekt.getNazwa());
        projektRepository.save(projectCreate);
        searchService.indexProject(projectCreate);
        log.info("Creating a new project: {}", projectCreate);
        return projectCreate;
    }


    @Override
    public void deleteProjekt(UUID projektId) {
        try {
            log.info("Deleting project with ID: {}", projektId);

            if (!projektRepository.existsById(projektId)) {
                log.error("No project found with ID: {}", projektId);
                throw new IllegalArgumentException("Project with ID " + projektId + " does not exist");
            }
            elasticsearchRepository.deleteProjectIdxByProjectId(projektId);

            zadanieRepository.findProjectTask(projektId).forEach(zadanie -> {
                zadanieRepository.deleteAll(zadanieRepository.findProjectTask(projektId));
                projektRepository.deleteById(projektId);
                log.info("Successfully deleted project with ID: {}", projektId);
            });
        } catch (Exception e) {
            log.error("Error occurred while deleting project with ID: {}", projektId, e);
            throw e;
        }
    }

    @Override
    public Page<Project> getProjekty(Pageable pageable) {
        return projektRepository.findAll(pageable);
    }

    @Override
    public boolean updateProjekt(UUID projektId, ProjektUpdateDTO updatedProject) {
        log.info("Attempting to update project with ID: {}", projektId);

        Optional<Project> projectOptional = projektRepository.findById(projektId);

        if (projectOptional.isPresent()) {
            Project projekt = projectOptional.get();
            projekt.setName(updatedProject.getNazwa());
            projekt.setDescription(updatedProject.getOpis());
            projektRepository.save(projekt);
            log.info("Project updated successfully with ID: {}", projektId);
            return true;
        } else {
            log.error("Project not found with ID: {}", projektId);
            return false;
        }
    }
}