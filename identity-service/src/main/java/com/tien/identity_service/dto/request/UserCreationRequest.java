package com.tien.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.tien.identity_service.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    @DobConstraint(min = 13, message = "INVALID_DOB")
    LocalDate dob;

    String city;
}
