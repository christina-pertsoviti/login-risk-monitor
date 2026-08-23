package com.loginriskmonitor.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String roleName) {
        super("Role '" + roleName + "' was not found");
    }
}
