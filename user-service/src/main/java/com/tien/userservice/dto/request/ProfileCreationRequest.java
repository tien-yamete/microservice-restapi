package com.tien.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreationRequest {
    private String Userid;
    private String username;
    private String phone;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;
}
