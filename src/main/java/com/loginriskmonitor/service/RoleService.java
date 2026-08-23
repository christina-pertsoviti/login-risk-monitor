package com.loginriskmonitor.service;

import com.loginriskmonitor.dto.RoleReadOnlyDTO;
import com.loginriskmonitor.mapper.RoleMapper;
import com.loginriskmonitor.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleReadOnlyDTO> getAllRoles() {
        return roleRepository.findAllByOrderByNameAsc()
                .stream()
                .map(roleMapper::toReadOnlyDTO)
                .toList();
    }
}
