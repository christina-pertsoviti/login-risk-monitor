package com.loginriskmonitor.service;

import com.loginriskmonitor.domain.Role;
import com.loginriskmonitor.dto.RoleReadOnlyDTO;
import com.loginriskmonitor.mapper.RoleMapper;
import com.loginriskmonitor.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Test
    void shouldReturnMappedRolesInRepositoryOrder() {
        Role role = new Role("ADMIN");
        RoleReadOnlyDTO roleDTO = new RoleReadOnlyDTO(1L, "ADMIN");

        when(roleRepository.findAllByOrderByNameAsc())
                .thenReturn(List.of(role));
        when(roleMapper.toReadOnlyDTO(role))
                .thenReturn(roleDTO);

        List<RoleReadOnlyDTO> result = roleService.getAllRoles();

        assertEquals(List.of(roleDTO), result);
    }
}
