package pl.edu.pbs.programowaniezwinne_be.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTask;

public interface ZadanieRepository extends JpaRepository<ProjectTask, UUID> {

	@Query("SELECT z FROM ProjectTask z WHERE z.project.id = :id")
	List<ProjectTask> findProjectTask(@Param("id") UUID id);

}
