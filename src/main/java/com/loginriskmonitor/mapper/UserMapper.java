package com.loginriskmonitor.mapper;

import com.loginriskmonitor.domain.User;
import com.loginriskmonitor.dto.UserReadOnlyDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserReadOnlyDTO toReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getId(),
                user.getUsername(),
                user.getRole().getName()
        );
    }
}
