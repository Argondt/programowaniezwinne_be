package pl.edu.pbs.programowaniezwinne_be.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.repository.ProjektRepository;
import pl.edu.pbs.programowaniezwinne_be.repository.ZadanieRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProjektServiceTest {

    @Mock
    private ProjektRepository projektRepository;

    @Mock
    private ZadanieRepository zadanieRepository;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private ProjektServiceImpl projektService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProjekt() {
        UUID testId = UUID.randomUUID();
        when(projektRepository.findById(testId)).thenReturn(Optional.of(new Project()));
        Optional<Project> result = projektService.getProjekt(testId);
        assertTrue(result.isPresent());
        verify(projektRepository).findById(testId);
    }

    @Test
    public void testCreateProject() {
        ProjektDTO updateDTO = ProjektDTO.builder().nazwa("Project Name").opis("Project Description").build();

        Project project = new Project();
        when(projektRepository.save(any(Project.class))).thenReturn(project);
        Project createdProject = projektService.createProject(updateDTO);
        assertNotNull(createdProject);
        verify(projektRepository).save(any(Project.class));
        verify(searchService).indexProject(any(Project.class));
    }

    @Test
    public void testCreateProjectWithNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.createProject(null);
        });
        assertTrue(exception.getMessage().contains("Project cannot be null"));
    }



    @Test
    public void testDeleteNonExistentProjekt() {
        UUID testId = UUID.randomUUID();
        when(projektRepository.existsById(testId)).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            projektService.deleteProjekt(testId);
        });
        assertTrue(exception.getMessage().contains("Project with ID " + testId + " does not exist"));
    }

    @Test
    public void testGetProjekty() {
        Pageable pageable = PageRequest.of(0, 10);
        when(projektRepository.findAll(pageable)).thenReturn(Page.empty());
        Page<Project> result = projektService.getProjekty(pageable);
        assertNotNull(result);
        verify(projektRepository).findAll(pageable);
    }

    @Test
    public void testUpdateProjekt() {
        UUID testId = UUID.randomUUID();
        ProjektUpdateDTO updateDTO = ProjektUpdateDTO.builder().nazwa("Updated Name").opis("Updated Descriptio").build();
        Project project = new Project();

        when(projektRepository.findById(testId)).thenReturn(Optional.of(project));
        when(projektRepository.save(any(Project.class))).thenReturn(project);

        boolean result = projektService.updateProjekt(testId, updateDTO);
        assertTrue(result);
        verify(projektRepository).save(project);
    }

    @Test
    public void testUpdateNonExistentProjekt() {
        UUID testId = UUID.randomUUID();
        ProjektUpdateDTO updateDTO = ProjektUpdateDTO.builder().nazwa("Updated Name").opis("Updated Descriptio").build();
        when(projektRepository.findById(testId)).thenReturn(Optional.empty());

        boolean result = projektService.updateProjekt(testId, updateDTO);
        assertFalse(result);
    }

}
