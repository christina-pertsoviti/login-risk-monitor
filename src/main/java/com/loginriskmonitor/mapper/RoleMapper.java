package com.loginriskmonitor.mapper;

import com.loginriskmonitor.domain.Role;
import com.loginriskmonitor.dto.RoleReadOnlyDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleReadOnlyDTO toReadOnlyDTO(Role role) {
        return new RoleReadOnlyDTO(role.getId(), role.getName());
    }
}
