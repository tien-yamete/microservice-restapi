package com.tien.userservice.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tien.userservice.dto.request.ProfileCreationRequest;
import com.tien.userservice.dto.response.ProfileResponse;
import com.tien.userservice.entity.Profile;
import com.tien.userservice.mapper.ProfileMapper;
import com.tien.userservice.repository.ProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileService {

    ProfileRepository profileRepository;

    ProfileMapper profileMapper;

    public ProfileResponse createProfile(ProfileCreationRequest request) {
        Profile userProfile = profileMapper.toProfile(request);
        userProfile = profileRepository.save(userProfile);

        return profileMapper.toProfileResponse(userProfile);
    }

    public ProfileResponse getProfile(String id) {
        Profile userProfile =
                profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));

        return profileMapper.toProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();

        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }
}
