package com.loginriskmonitor.service;

import com.loginriskmonitor.dto.RoleReadOnlyDTO;

import java.util.List;

public interface IRoleService {

    List<RoleReadOnlyDTO> getAllRoles();
}
