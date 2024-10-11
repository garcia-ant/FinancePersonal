package com.gposter.minhasfinancas.model.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gposter.minhasfinancas.model.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")  // Corrigido para "test" em minúsculas
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void VerificarEmail() {
        // Cenário
        User user = userCreate();
        entityManager.persist(user);

        // Ação e execução
        boolean result = repository.existsByEmail("user@email.com");

        // Verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void VerificarEmailSeOEmailNaoExistir() {
        // Ação e execução
        boolean result = repository.existsByEmail("user@gmail.com");

        // Verificação
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        // Cenário
        User user = userCreate();

        // Ação
        User userSave = repository.save(user);

        // Verificação
        Assertions.assertThat(userSave.getId()).isNotNull();
    }

    @Test
    public void DeveBuscarUmUsuarioPorEmail() {
        // Cenário
        User user = userCreate();
        entityManager.persist(user);  // Persistir o usuário antes de buscar

        // Ação
        Optional<User> result = repository.findByEmail("user@email.com");

        // Verificação
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    // Método auxiliar para criar um usuário
    public static User userCreate() {
        return User.builder()
            .nome("user")
            .email("user@email.com")
            .senha("senha")
            .build();
    }
}
