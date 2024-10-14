package com.gposter.minhasfinancas.service;

import java.util.Optional;

import com.gposter.minhasfinancas.model.entities.User;

public interface UserService {
	
	User authenticate(String email , String senha);

	User saveUser(User user);
	
	void validateEmail(String email);
	
	Optional<User> obterPorId(Long id);
	

}
