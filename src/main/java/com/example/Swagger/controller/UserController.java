package com.example.Swagger.controller;

import com.example.Swagger.dto.UserRequestDTO;
import com.example.Swagger.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<UserResponseDTO> createUser(UserRequestDTO userRequestDTO);
    ResponseEntity<UserResponseDTO> getUserById(Long id);
    ResponseEntity<List<UserResponseDTO>> getAllUsers();
    ResponseEntity<UserResponseDTO> updateUser(Long id, UserRequestDTO userRequestDTO);
    ResponseEntity<Void> deleteUser(Long id);
}