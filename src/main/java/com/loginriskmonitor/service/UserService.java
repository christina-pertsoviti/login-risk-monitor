package com.loginriskmonitor.service;

import com.loginriskmonitor.domain.Role;
import com.loginriskmonitor.domain.User;
import com.loginriskmonitor.dto.UserCreateDTO;
import com.loginriskmonitor.dto.UserReadOnlyDTO;
import com.loginriskmonitor.exception.RoleNotFoundException;
import com.loginriskmonitor.exception.UsernameAlreadyExistsException;
import com.loginriskmonitor.mapper.UserMapper;
import com.loginriskmonitor.repository.RoleRepository;
import com.loginriskmonitor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserReadOnlyDTO> getAllUsers() {
        return userRepository.findAllByOrderByUsernameAsc()
                .stream()
                .map(userMapper::toReadOnlyDTO)
                .toList();
    }

    @Override
    @Transactional
    public void createUser(UserCreateDTO userCreateDTO) {
        String username = userCreateDTO.getUsername().strip();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        String roleName = userCreateDTO
                .getRoleName()
                .strip()
                .toUpperCase(Locale.ROOT);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        User user = new User(
                username,
                passwordEncoder.encode(userCreateDTO.getPassword()),
                role
        );

        userRepository.save(user);
    }
}
