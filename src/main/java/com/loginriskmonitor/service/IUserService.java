package com.loginriskmonitor.service;

import com.loginriskmonitor.dto.UserCreateDTO;
import com.loginriskmonitor.dto.UserReadOnlyDTO;

import java.util.List;

public interface IUserService {

    List<UserReadOnlyDTO> getAllUsers();

    void createUser(UserCreateDTO userCreateDTO);
}
