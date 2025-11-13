package com.user.user_registration.models;

public record RegisterDTO(String name, Integer age, String login, String password, UserRole role ) {
}
