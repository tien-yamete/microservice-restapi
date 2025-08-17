package com.tien.identity_service.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tien.identity_service.dto.request.RoleRequest;
import com.tien.identity_service.dto.response.RoleResponse;
import com.tien.identity_service.mapper.RoleMapper;
import com.tien.identity_service.repository.PermissionRepository;
import com.tien.identity_service.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

// RoleService: Xử lý nghiệp vụ liên quan đến Role:
//          - Tạo role mới và gán danh sách permission.
//          - Lấy danh sách toàn bộ role.
//          - Xóa role theo tên.

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;

    PermissionRepository permissionRepository;

    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
