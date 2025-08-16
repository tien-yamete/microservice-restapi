package com.tien.identity_service.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.tien.identity_service.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 13, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}
