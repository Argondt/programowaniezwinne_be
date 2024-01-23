package pl.edu.pbs.programowaniezwinne_be.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.pbs.programowaniezwinne_be.model.Project;

public interface ProjektRepository extends JpaRepository<Project, UUID> {

}