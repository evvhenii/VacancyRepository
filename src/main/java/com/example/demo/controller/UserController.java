package com.example.demo.controller;

import com.example.demo.dto.*;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.config.jwt.JwtProvider;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import java.security.Principal;
import java.util.Optional;

@Log
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;

    @GetMapping
    public String showInfo() {
        return "<h1>This is vacancy diary</h1><h2>Please, read <a href=\"https://github.com/evvhenii/VacancyRepository\">READMI</a></h2>";
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
        User user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        String token = jwtProvider.generateToken(user.getId());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PutMapping("/update_user")
    public ResponseEntity<User> updateUser(@RequestBody UpdateProfileRequest updateProfileRequest, Principal principal) {
        log.info("Handling updating user");
        int id = Integer.parseInt(principal.getName());
        User user = userService.findById(id).get();
        user.setName(updateProfileRequest.getName());
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my_profile")
    public ResponseEntity<GetProfileResponse> showProfile(Principal principal) {
        log.info("Handling get user information request");
        int id = Integer.parseInt(principal.getName());
        Optional<User> optUser = userService.findById(id);
        return optUser.map(user -> modelMapper.map(optUser.get(), GetProfileResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @DeleteMapping("/my_profile")
    public ResponseEntity<Void> delete(Principal principal) {
        log.info("Handling delete user request");
        int id = Integer.parseInt(principal.getName());
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}