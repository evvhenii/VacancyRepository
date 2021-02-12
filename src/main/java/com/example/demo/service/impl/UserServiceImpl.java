package com.example.demo.service.impl;

import java.util.Optional;

import com.example.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

	@Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
	
	@Override
    public void updateUser(User user) {
	    userRepository.save(user);
    }
	
	@Override
    public Optional<User> findById(Integer userId) {
		return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    @Override
	public User findByEmailAndPassword(String email, String password) {
		Optional<User> optUser = findByEmail(email);
		if(optUser.isEmpty()) return null;
		User user = optUser.get();
		if (passwordEncoder.matches(password, user.getPassword())) {
			return user;
		}
		return null;
	}
}
