package com.tien.identity_service.mapper;

import org.mapstruct.Mapper;

import com.tien.identity_service.dto.request.ProfileCreationRequest;
import com.tien.identity_service.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
