package com.jardimbotanico.backend.repository;

import com.jardimbotanico.backend.model.Credenciais;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
public class CredenciaisRepositoryTest {

    @BeforeEach
    void setUp() {
        credenciaisRepository.deleteAll();
    }

    @Autowired
    private CredenciaisRepository credenciaisRepository;

    // Teste usando banco mongo local
    @Test
    @DisplayName("Usuário presente no banco de dados.")
    public void testFindByUsername() {
        System.out.println("Test started");  // Log simples para testar execução do teste

        // Cria e salva uma credencial de teste no MongoDB
        Credenciais credenciais = new Credenciais();
        credenciais.setUsername("usuario");
        credenciais.setPassword("senhaSegura");
        credenciaisRepository.save(credenciais);

        // Tenta recuperar a credencial pelo username
        Optional<Credenciais> credenciaisOpt = credenciaisRepository.findByUsername("usuario");

        // Verifica se a credencial foi recuperada corretamente
        assertTrue(credenciaisOpt.isPresent());
        assertEquals("usuario", credenciaisOpt.get().getUsername());
        assertEquals("senhaSegura", credenciaisOpt.get().getPassword());

    }

    @Test
    @DisplayName("Usuário não presente no banco de dados.")
    public void testFindByUsernameNotFound() {
        // Tenta recuperar uma credencial inexistente do MongoDB
        Optional<Credenciais> credenciaisOpt = credenciaisRepository.findByUsername("usuarioInvalido");

        // Verifica se não encontrou a credencial
        assertFalse(credenciaisOpt.isPresent());
    }
}
