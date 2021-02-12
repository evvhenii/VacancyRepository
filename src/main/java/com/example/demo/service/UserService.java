package com.example.demo.service;

import java.util.Optional;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationException;

public interface UserService {
	
	User saveUser(User user);

    void deleteUser(int userId);

    Optional<User> findByEmail(String email);

	User findByEmailAndPassword(String email, String password);

	Optional<User> findById(Integer userId);

	void updateUser(User user);
}
