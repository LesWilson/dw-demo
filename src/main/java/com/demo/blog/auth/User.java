package com.demo.blog.auth;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;

@Getter
@Setter
public class User implements Principal {
    private int id;
    private String username;
    private String password;

    @Override
    public String getName() {
        return username;
    }
}