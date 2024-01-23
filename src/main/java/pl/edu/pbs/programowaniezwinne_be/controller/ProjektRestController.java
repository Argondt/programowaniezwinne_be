package pl.edu.pbs.programowaniezwinne_be.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pbs.programowaniezwinne_be.Dto.DownloadUrlDto;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectFileDto;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjektUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.FileProject;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.model.elastic.ProjectIdx;
import pl.edu.pbs.programowaniezwinne_be.repository.FileProjectRepository;
import pl.edu.pbs.programowaniezwinne_be.repository.ProjektRepository;
import pl.edu.pbs.programowaniezwinne_be.service.ProjektService;
import pl.edu.pbs.programowaniezwinne_be.service.SearchService;
import pl.edu.pbs.programowaniezwinne_be.service.s3.MinioService;


@RestController
@RequestMapping("/api")
@AllArgsConstructor// adnotacja
public class ProjektRestController {
    private final MinioService minioService;
    private final FileProjectRepository repository;

    private final ProjektRepository projektRepository;
    private ProjektService projektService;
    private final SearchService searchService;


    @GetMapping("/projekty/{projektId}")
    ResponseEntity<Project> getProjekt(@PathVariable UUID projektId) {// @PathVariable oznacza, że wartość
        return ResponseEntity.of(projektService.getProjekt(projektId)); // parametru przekazywana jest w ścieżce
    }

    @PostMapping(path = "/projekty")
    ResponseEntity<Void> createProjekt(@Valid @RequestBody ProjektDTO projekt) {
        projektService.createProject(projekt);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/projekty/{projektId}")
    public ResponseEntity<?> updateProject(@Valid @RequestBody ProjektUpdateDTO projekt, @PathVariable UUID projektId) {
        boolean updateSuccessful = projektService.updateProjekt(projektId, projekt);

        if (updateSuccessful) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/projekty/{projektId}/upload")
    public ResponseEntity<?> uploadFileToProject(@PathVariable UUID projektId,
                                                 @RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String projektPlik = minioService.add(filename, file, projektId);
            return ResponseEntity.ok(projektPlik);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    @DeleteMapping("/projekty/{projektId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projektId) {
        return projektService.getProjekt(projektId).map(p -> {
            projektService.deleteProjekt(projektId);
            return new ResponseEntity<Void>(HttpStatus.OK); // 200
        }).orElseGet(() -> ResponseEntity.notFound().build()); // 404 - Not found
    }


    @GetMapping(value = "/projekty")
    Page<ProjectIdx> getProjectByName(@RequestParam(required = false) String nazwa, Pageable pageable) {
        return searchService.searchByName(nazwa, pageable);
    }

    @GetMapping("/projekty/{projektId}/files/{filename}/download")
    public ResponseEntity<?> downloadFile(@PathVariable UUID projektId, @PathVariable String filename) {
        try {
            String downloadUrl = minioService.getPreSignedUrl(filename);
            return ResponseEntity.ok(new DownloadUrlDto(downloadUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating download URL");
        }
    }

    @GetMapping("/projekty/{projektId}/files")
    public ResponseEntity<List<ProjectFileDto>> getProjectFiles(@PathVariable UUID projektId) {
        try {
            Optional<Project> projekt = projektRepository.findById(projektId);
            List<FileProject> pliki = repository.findByProject(projekt.get());
            List<ProjectFileDto> plikiDTO = pliki.stream()
                    .map(plik -> new ProjectFileDto(plik.getId(), plik.getNazwaPliku(), plik.getUrl()))
                    .toList();
            return ResponseEntity.ok(plikiDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
