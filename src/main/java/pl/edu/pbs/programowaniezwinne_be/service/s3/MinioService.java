package pl.edu.pbs.programowaniezwinne_be.service.s3;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pbs.programowaniezwinne_be.model.FileProject;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.repository.FileProjectRepository;
import pl.edu.pbs.programowaniezwinne_be.repository.ProjektRepository;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final FileProjectRepository repository;
    private final ProjektRepository projektRepository;

    public MinioService(@Value("${minio.url}") String url,
                        @Value("${minio.access-key}") String accessKey,
                        @Value("${minio.secret-key}") String secretKey,
                        @Value("${minio.bucket-name}") String bucketName, FileProjectRepository repository, ProjektRepository projektRepository) {
        this.repository = repository;
        this.projektRepository = projektRepository;
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
    }

    public String add(String filename, MultipartFile file, UUID projektId) {
        try {
            FileProject fileProject = new FileProject();
            fileProject.setNazwaPliku(filename);
            fileProject.setUrl(getPreSignedUrl(filename));
            Optional<Project> projekt = projektRepository.findById(projektId);
            Project project1 = projekt.get();
            fileProject.setProject(project1);
            repository.save(fileProject);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());

            log.info("Uploaded file {} to {} bucket", filename, bucketName);
        } catch (Exception e) {
            throw new RuntimeException(filename);
        }

        return getPreSignedUrl(filename);
    }

    public String getPreSignedUrl(String filename) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(filename)
                            .expiry(60) // Ustaw czas ważności linku
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating URL for file " + filename, e);
        }
    }
}
