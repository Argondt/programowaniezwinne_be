package pl.edu.pbs.programowaniezwinne_be.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.Project;

public interface ProjektService {
    Optional<Project> getProjekt(UUID projektId);

    Project createProject(ProjektDTO projekt);

    void deleteProjekt(UUID projektId);

    Page<Project> getProjekty(Pageable pageable);


    boolean updateProjekt(UUID projektId, ProjektUpdateDTO updatedProject);
}
