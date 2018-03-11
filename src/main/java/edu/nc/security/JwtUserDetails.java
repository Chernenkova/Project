package edu.nc.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUserDetails {

    public static String getUserName() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
