package com.user.user_registration.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateDTO(@Size(min = 3) String name, @Min(1) Integer age, @Email String login, @Size(min = 6) String password) {
}
