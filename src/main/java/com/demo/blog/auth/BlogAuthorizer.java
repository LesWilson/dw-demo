package com.demo.blog.auth;

import java.util.Objects;

import io.dropwizard.auth.Authorizer;

public class BlogAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User principal, String role) {
        // Allow any logged in user.
        return Objects.nonNull(principal);
    }
}
