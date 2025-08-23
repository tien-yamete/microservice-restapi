package com.tien.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tien.userservice.entity.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByUserId(String userId);
    List<Profile> findAllByUsernameLike(String username);
}
