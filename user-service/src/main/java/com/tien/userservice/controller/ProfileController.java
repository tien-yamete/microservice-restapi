package com.tien.userservice.controller;

import com.tien.userservice.dto.ApiResponse;
import com.tien.userservice.dto.request.SearchProfileRequest;
import com.tien.userservice.dto.request.UpdateProfileRequest;
import com.tien.userservice.dto.response.ProfileResponse;
import com.tien.userservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequiredArgsConstructor
public class ProfileController {
    ProfileService profileService;

    @GetMapping("/users/{profileId}")
    ApiResponse<ProfileResponse> getProfile(@PathVariable String profileId){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getProfile(profileId))
                .build();
    }

    @GetMapping("/users")
    ApiResponse<List<ProfileResponse>> getAllProfiles(){
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.getAllProfile())
                .build();
    }

    @GetMapping("/users/my-profile")
    ApiResponse<ProfileResponse> updateMyProfile(@RequestBody UpdateProfileRequest request){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.updateMyProfile(request))
                .build();
    }

    @GetMapping("/users/avatar")
    ApiResponse<ProfileResponse> updateAvatar(@RequestParam("file") MultipartFile file){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.updateAvatar(file))
                .build();
    }

    @PostMapping("/users/search")
    ApiResponse<List<ProfileResponse>> search(@RequestBody SearchProfileRequest request){
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.search(request))
                .build();
    }
}
