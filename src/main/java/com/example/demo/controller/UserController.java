package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.NotAuthenticatedException;
import com.example.demo.exception.UserNotFoundException;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Log
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public String showInfo() {
        return "<h1>This is vacancy diary</h1><h2>Please, read <a target=\"_blank\" href=\"https://github.com/evvhenii/VacancyRepository\">README</a></h2>";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CreateProfileRequest createProfileRequest) {
        log.info("Handling user registration");
        User user = modelMapper.map(createProfileRequest, User.class);
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        log.info("Handling authorization user");

        try{
            AuthResponse authResponse = new AuthResponse(userService.authenticate(request.getEmail(), request.getPassword()));
            return ResponseEntity.ok(authResponse);
        }catch (NotAuthenticatedException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Prove credentials", ex);
        }
    }

    @PutMapping("/update_user")
    public ResponseEntity<User> updateUser(@RequestBody UpdateProfileRequest updateProfileRequest) {
        log.info("Handling updating user");
        User user = modelMapper.map(updateProfileRequest, User.class);
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my_profile")
    public ResponseEntity<GetProfileResponse> showProfile() {
        log.info("Handling get user information request");
        try{
            User user = userService.getCurrentUser();
            GetProfileResponse userProfile = modelMapper.map(user, GetProfileResponse.class);
            return ResponseEntity.ok(userProfile);
        } catch(UserNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Prove credentials", ex);
        }

    }

    @DeleteMapping("/my_profile")
    public ResponseEntity<Void> delete() {
        log.info("Handling delete user request");
        userService.deleteUser();
        return ResponseEntity.ok().build();
    }
}