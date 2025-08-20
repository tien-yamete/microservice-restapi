package com.tien.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tien.userservice.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {}
