package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.NotAuthenticatedException;
import com.example.demo.exception.UserNotFoundException;

public interface UserService {
	User saveUser(User user);
    void deleteUser();
	void updateUser(User user);
	String authenticate(String email, String password) throws NotAuthenticatedException;
	User getCurrentUser() throws UserNotFoundException;
}
