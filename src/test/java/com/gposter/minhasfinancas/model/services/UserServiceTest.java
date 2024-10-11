package com.gposter.minhasfinancas.model.services;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gposter.minhasfinancas.exception.ErrorAuthentication;
import com.gposter.minhasfinancas.exception.RuleBusinessException;
import com.gposter.minhasfinancas.model.entities.User;
import com.gposter.minhasfinancas.model.repositories.UserRepository;
import com.gposter.minhasfinancas.service.impl.UserServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

	@SpyBean
    UserServiceImpl service;

    @MockBean
    UserRepository repository;

 
    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        // Cenário
        String email = "email@mail.com";
        String senha = "senha";

        // Criando um usuário simulado com o email e senha
        User user = User.builder().email(email).senha(senha).id(1L).build();

        // Mockando o comportamento do repositório para retornar o usuário
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        // Ação: autenticando o usuário
        User result = service.authenticate(email, senha);

        // Verificações:
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
        // Cenário: nenhum usuário encontrado com o email fornecido
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        // Ação e Verificação: espera-se que a exceção ErrorAuthentication seja lançada
        Throwable exception = Assertions.catchThrowable(() -> service.authenticate("email@mail.com", "senha"));

        Assertions.assertThat(exception).isInstanceOf(ErrorAuthentication.class)
                .hasMessage("Usuário não encontrado para email informado.");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        // Cenário
        String senha = "senha";
        User user = User.builder().email("mail@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        // Ação
        Throwable exception = Assertions.catchThrowable(() -> service.authenticate("mail@email.com", "senha_errada"));

        // Verificação
        Assertions.assertThat(exception).isInstanceOf(ErrorAuthentication.class)
                .hasMessage("Senha inválida.");
    }

   
    @Test
    public void deveValidarEmail() {
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        
        //ação
        service.validateEmail("email@email.com");
        
        //nenhuma exceção esperada
    }
    
    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        
        //ação
        org.junit.jupiter.api.Assertions
            .assertThrows(RuntimeException.class, () -> service.validateEmail("email@email.com"));
    }
    
    
    @Test
	public void deveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(service).validateEmail(Mockito.anyString());   
		User user = User.builder()
					.id(1l)
					.nome("nome")
					.email("email@email.com")
					.senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);
		
		//acaosalvarUsuario
		User userSave = service.saveUser(new User());
		
		//verificao
		Assertions.assertThat(userSave).isNotNull();
		Assertions.assertThat(userSave.getId()).isEqualTo(1l);
		Assertions.assertThat(userSave.getNome()).isEqualTo("nome");
		Assertions.assertThat(userSave.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(userSave.getSenha()).isEqualTo("senha");
		
	}
    
    
    
    @Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//cenario
		String email = "email@email.com";
		User user = User.builder().email(email).build();
		Mockito.doThrow(RuleBusinessException.class).when(service).validateEmail(email);
		
		//acao
		Throwable exception = Assertions.catchThrowable( () ->service.saveUser(user));
		 Assertions.assertThat(exception).isInstanceOf(RuleBusinessException.class);
		
		//verificacao
		Mockito.verify( repository, Mockito.never() ).save(user);
		
	}
	
}
