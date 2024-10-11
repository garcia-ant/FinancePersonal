package com.gposter.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gposter.minhasfinancas.api.dto.UserDTO;
import com.gposter.minhasfinancas.exception.ErrorAuthentication;
import com.gposter.minhasfinancas.exception.RuleBusinessException;
import com.gposter.minhasfinancas.model.entities.User;
import com.gposter.minhasfinancas.service.UserService;

@RestController
@RequestMapping("/api/usuarios")
public class UserResource {

	private final UserService service;

	// Construtor para injetar dependencias o serviço
	public UserResource(UserService service) {
		this.service = service;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity authenticate(@RequestBody UserDTO dto ) {
		
		try {
		User UserAuthenticated =service.authenticate(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(UserAuthenticated);
			
		} catch (ErrorAuthentication e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		
	}

	// Método para salvar um usuário
	@PostMapping
	public ResponseEntity save(@RequestBody UserDTO dto) {

		// Converter UserDTO para a entidade User
		User user = User.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()) // Usando 'senha' como está no DTO
				.build();
		try {
			// Salvar o usuário utilizando o serviço
			User savedUser = service.saveUser(user);

			// Retornar o usuário salvo com status HTTP 201 (CREATED)
			return new ResponseEntity (savedUser, HttpStatus.CREATED);
		} catch (RuleBusinessException e) {
			// Em caso de erro, retornar o status HTTP 400 (BAD REQUEST) com a mensagem de erro
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
