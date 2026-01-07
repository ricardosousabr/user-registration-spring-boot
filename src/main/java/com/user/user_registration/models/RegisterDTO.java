package com.user.user_registration.models;

public record RegisterDTO(String name, Integer age, String email, String password, UserRole role ) {
}
