package com.tien.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "userId", nullable = false, unique = true)
    private String userId;

    private String avatar;
    private String username;
    private String phone;
    private String email;

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;

}
