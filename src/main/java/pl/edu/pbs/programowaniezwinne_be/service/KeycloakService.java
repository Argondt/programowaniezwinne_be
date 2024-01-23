package pl.edu.pbs.programowaniezwinne_be.service;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.pbs.programowaniezwinne_be.Dto.StudentDetailsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakService {

    private final Keycloak keycloak;
    private final String targetRealm;

    public KeycloakService(
            @Value("${keycloak.server-url}") String serverUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.username}") String username,
            @Value("${keycloak.password}") String password,
            @Value("${keycloak.client-id}") String clientId, @Value("${keycloak.target-realm}")String targetRealm) {
        this.targetRealm = targetRealm;

        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .build();
    }

    public List<StudentDetailsDto> getUsers() {
        List<UserRepresentation> users = keycloak.realm(targetRealm).users().list();

        List<StudentDetailsDto> userDetailsList = new ArrayList<>();

        for (UserRepresentation user : users) {
            List<RoleRepresentation> userRoles = keycloak.realm(targetRealm).users().get(user.getId()).roles().realmLevel().listEffective();
            StudentDetailsDto userDetails = getStudentDetailsDto(user, userRoles);

            userDetailsList.add(userDetails);
        }
        return userDetailsList;
    }



    public StudentDetailsDto getUserById(String userId) {
        UserRepresentation keycloakUser = keycloak.realm(targetRealm).users().get(userId).toRepresentation();
        List<RoleRepresentation> userRoles = keycloak.realm(targetRealm).users().get(userId).roles().realmLevel().listEffective();

        return getStudentDetailsDto(keycloakUser, userRoles);
    }



    public void updateRole(String userId, String roleName) {
        UsersResource  keycloakUser = keycloak.realm(targetRealm).users();
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        keycloakUser.get(userId).roles().realmLevel().add(List.of(role));
    }

    public void updateUser(String userId, StudentDetailsDto userDTO) {
        UsersResource usersResource = keycloak.realm(targetRealm).users();
        UserResource userResource;

        try {
            userResource = usersResource.get(userId);
            if (userResource == null) {
                throw new NotFoundException("User not found with ID: " + userId);
            }

            UserRepresentation user = userResource.toRepresentation();
            user.setUsername(userDTO.getUsername());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            userResource.update(user);

            List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();

            userResource.roles().realmLevel().remove(currentRoles);

            List<RoleRepresentation> newRoles = userDTO.getRoles().stream()
                    .map(roleName -> {
                        RoleRepresentation role = keycloak.realm(targetRealm).roles().get(roleName).toRepresentation();
                        if (role == null) {
                            throw new NotFoundException("Role not found: " + roleName);
                        }
                        return role;
                    }).collect(Collectors.toList());

            userResource.roles().realmLevel().add(newRoles);

        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteUser(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .remove();
    }
    public UsersResource getInstance(){
        return  keycloak.realm(targetRealm).users();
    }
    public void createGroup(String groupName) {

        RealmResource realmResource = keycloak.realm(targetRealm);
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);
        Response response = keycloak.realm(targetRealm).groups().add(groupRepresentation);
        log.info("Status odpowiedzi przy tworzeniu grupy: " + response.getStatus());
        log.info("Informacje o statusie: " + response.getStatusInfo());

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            String responseBody = response.readEntity(String.class);
            log.error("Błąd przy tworzeniu grupy: " + responseBody);
        }
        response.close();
    }
    @NotNull
    private static StudentDetailsDto getStudentDetailsDto(UserRepresentation user, List<RoleRepresentation> userRoles) {
        StudentDetailsDto userDetails = new StudentDetailsDto();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
        userDetails.setEmail(user.getEmail());
        userDetails.setRoles(convertRolesToNames(userRoles));
        return userDetails;
    }
    @NotNull
    private static List<String> convertRolesToNames(List<RoleRepresentation> userRoles) {
        return userRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toList());
    }
}