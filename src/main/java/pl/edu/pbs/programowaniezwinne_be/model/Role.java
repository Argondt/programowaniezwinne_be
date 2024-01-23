package pl.edu.pbs.programowaniezwinne_be.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER("użytkownik"), ADMIN("Administaror");
    private final String displayName;

    public static Role fromDisplayName(String displayName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getDisplayName().equals(displayName))
                .findFirst()
                .orElse(null);
    }

}
