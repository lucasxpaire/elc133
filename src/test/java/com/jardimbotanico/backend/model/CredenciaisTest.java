package com.jardimbotanico.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CredenciaisTest {

    @Test
    public void testCredenciais() {
        // Cria um objeto Credenciais
        Credenciais credenciais = new Credenciais();

        // Define valores usando setters
        credenciais.setId("1");
        credenciais.setUsername("usuario");
        credenciais.setPassword("senhaSegura");

        // Verifica se os valores estão corretos através dos getters
        assertEquals("1", credenciais.getId());
        assertEquals("usuario", credenciais.getUsername());
        assertEquals("senhaSegura", credenciais.getPassword());
    }

}
