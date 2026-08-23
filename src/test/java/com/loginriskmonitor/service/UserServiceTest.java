package com.loginriskmonitor.service;

import com.loginriskmonitor.domain.Role;
import com.loginriskmonitor.domain.User;
import com.loginriskmonitor.dto.UserCreateDTO;
import com.loginriskmonitor.dto.UserReadOnlyDTO;
import com.loginriskmonitor.exception.UsernameAlreadyExistsException;
import com.loginriskmonitor.mapper.UserMapper;
import com.loginriskmonitor.repository.RoleRepository;
import com.loginriskmonitor.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithEncodedPasswordAndSelectedRole() {
        UserCreateDTO userCreateDTO = createUserDTO();
        Role role = new Role("USER");

        when(userRepository.existsByUsernameIgnoreCase("trainer1"))
                .thenReturn(false);
        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Secure123!"))
                .thenReturn("encoded-password");

        userService.createUser(userCreateDTO);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("trainer1", savedUser.getUsername());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
    }

    @Test
    void duplicateUsernameShouldBeRejected() {
        UserCreateDTO userCreateDTO = createUserDTO();

        when(userRepository.existsByUsernameIgnoreCase("trainer1"))
                .thenReturn(true);

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.createUser(userCreateDTO)
        );

        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldReturnMappedUsersInRepositoryOrder() {
        User user = new User("user", "encoded", new Role("USER"));
        UserReadOnlyDTO userDTO = new UserReadOnlyDTO(1L, "user", "USER");

        when(userRepository.findAllByOrderByUsernameAsc())
                .thenReturn(List.of(user));
        when(userMapper.toReadOnlyDTO(user))
                .thenReturn(userDTO);

        List<UserReadOnlyDTO> result = userService.getAllUsers();

        assertEquals(List.of(userDTO), result);
    }

    private UserCreateDTO createUserDTO() {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(" trainer1 ");
        userCreateDTO.setPassword("Secure123!");
        userCreateDTO.setRoleName("user");
        return userCreateDTO;
    }
}
