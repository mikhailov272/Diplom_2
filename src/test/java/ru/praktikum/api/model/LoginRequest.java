package ru.praktikum.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class LoginRequest {
    private final String email;
    @ToString.Exclude
    private final String password;
}
