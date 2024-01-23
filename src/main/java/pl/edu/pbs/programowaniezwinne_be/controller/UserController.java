package pl.edu.pbs.programowaniezwinne_be.controller;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pbs.programowaniezwinne_be.Dto.StudentDetailsDto;
import pl.edu.pbs.programowaniezwinne_be.service.KeycloakService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final KeycloakService keycloakUserService;

    public UserController(KeycloakService keycloakUserService) {
        this.keycloakUserService = keycloakUserService;
    }

    @GetMapping("")
    public List<StudentDetailsDto> getUsers() {
        return keycloakUserService.getUsers();
    }

    @GetMapping("/{userId}")
    public StudentDetailsDto getUserById(@PathVariable String userId) {
        return keycloakUserService.getUserById(userId);
    }

    @PutMapping(path = "/{userId}/update")
    public String updateUser(@PathVariable("userId") String userId, @RequestBody StudentDetailsDto userDTO){
        keycloakUserService.updateUser(userId, userDTO);
        return "User Details Updated Successfully.";
    }

    @DeleteMapping(path = "/{userId}/delete")
    public String deleteUser(@PathVariable("userId") String userId){
        keycloakUserService.deleteUser(userId);
        return "User Deleted Successfully.";
    }
    @PutMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<?> updateRole(@PathVariable String userId, @PathVariable String roleName) {
        keycloakUserService.updateRole(userId, roleName);
        return ResponseEntity.ok("Rola zaktualizowana");
    }
}
