package com.example.demo.service.impl;

import java.security.Principal;
import java.util.Optional;

import com.example.demo.config.jwt.JwtProvider;
import com.example.demo.entity.User;
import com.example.demo.exception.NotAuthenticatedException;
import com.example.demo.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final HttpServletRequest httpServletRequest;

    public int getUserIdFromSession(){
        Principal principal = httpServletRequest.getUserPrincipal();
        return Integer.parseInt(principal.getName());
    }

	@Override
    public void deleteUser() {
        int id = getUserIdFromSession();
	    userRepository.deleteById(id);
    }
	
	@Override
    public void updateUser(User user) {
        int id = getUserIdFromSession();
        User userPreviousVersion = findById(id).get();
        userPreviousVersion.setName(user.getName());
	    userRepository.save(userPreviousVersion);
    }

    @Override
    public String authenticate(String email, String password) throws NotAuthenticatedException {
        User user = findByEmailAndPassword(email, password);
        if(user == null) throw new NotAuthenticatedException();
        return jwtProvider.generateToken(user.getId());
    }

    @Override
    public User getCurrentUser() throws UserNotFoundException {
	    int id = getUserIdFromSession();
        Optional<User> optUser = findById(id);
        return optUser.orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> findById(int userId) {
		return userRepository.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
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
