package com.tien.userservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.tien.userservice.dto.ApiResponse;
import com.tien.userservice.dto.response.ProfileResponse;
import com.tien.userservice.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {

    ProfileService profileService;

    @GetMapping("/{profileId}")
    ApiResponse<ProfileResponse> getProfile(@PathVariable String profileId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getProfile(profileId))
                .build();
    }

    @GetMapping("/users")
    ApiResponse<List<ProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.getAllProfiles())
                .build();
    }
}
