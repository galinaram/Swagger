package com.example.Swagger.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private Integer age;
}