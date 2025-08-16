package com.tien.userservice.service;

import com.tien.userservice.dto.request.ProfileCreationRequest;
import com.tien.userservice.dto.request.SearchProfileRequest;
import com.tien.userservice.dto.request.UpdateProfileRequest;
import com.tien.userservice.dto.response.ProfileResponse;
import com.tien.userservice.entity.Profile;
import com.tien.userservice.exception.AppException;
import com.tien.userservice.exception.ErrorCode;
import com.tien.userservice.mapper.ProfileMapper;
import com.tien.userservice.repository.ProfileRepository;
import com.tien.userservice.repository.httpclient.FileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileService {

    ProfileRepository profileRepository;
    FileClient fileClient;
    ProfileMapper profileMapper;

    public ProfileResponse getByUserId(String userId){
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse createProfile(ProfileCreationRequest request){
        Profile profile = profileMapper.toUserProfile(request);
        profile = profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProfileResponse getProfile(String id){
        Profile profile = profileRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_EXISTED));

        return profileMapper.toProfileResponse(profile);
    }

    public List<ProfileResponse> getAllProfile(){
        var profiles = profileRepository.findAll();

        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    public ProfileResponse getMyProfile(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse updateMyProfile(UpdateProfileRequest request){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        profileMapper.update(profile, request);

        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }
    public ProfileResponse updateAvatar(MultipartFile file){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var response = fileClient.uploadMedia(file);

        profile.setAvatar(response.getResult().getUrl());

        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    public List<ProfileResponse> search(SearchProfileRequest request){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Profile> profiles = profileRepository.findAllByUsernameLike(request.getKeyword());

        return profiles.stream().filter(profile -> !userId.equals((profile.getUserId())))
                .map(profileMapper::toProfileResponse)
                .toList();
    }
}
