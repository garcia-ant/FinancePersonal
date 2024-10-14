package com.gposter.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gposter.minhasfinancas.exception.ErrorAuthentication;
import com.gposter.minhasfinancas.exception.RuleBusinessException;
import com.gposter.minhasfinancas.model.entities.User;
import com.gposter.minhasfinancas.model.repositories.UserRepository;
import com.gposter.minhasfinancas.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository repository;

	@Autowired
	public UserServiceImpl(UserRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public User authenticate(String email, String senha) {
	    Optional<User> user = repository.findByEmail(email);
	    
	    // Verifica se o usuário foi encontrado, se não, lança a exceção de autenticação
	    if (!user.isPresent()) {
	        throw new ErrorAuthentication("Usuário não encontrado para email informado.");
	    }
	    
	    // Verifica se a senha é correta (exemplo simplificado)
	    if (!user.get().getSenha().equals(senha)) {
	        throw new ErrorAuthentication("Senha inválida.");
	    }

	    // Retorna o usuário autenticado
	    return user.get();
	}



	@Override
	@Transactional
	public User saveUser(User user) {
		validateEmail(user.getEmail());
		return repository.save(user);
	}

	@Override
	public void validateEmail(String email) {
		boolean exist = repository.existsByEmail(email);
		if (exist) {
			throw new RuleBusinessException("There is already a user with this email");
		}

	}

	@Override
	public Optional<User> obterPorId(Long id) {	
		return repository.findById(id);
	}


}
