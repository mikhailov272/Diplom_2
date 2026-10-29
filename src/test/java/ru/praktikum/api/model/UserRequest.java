package ru.praktikum.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private String email;
    @ToString.Exclude
    private String password;
    private String name;
}
