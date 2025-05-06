package com.jardimbotanico.backend.controller;

import com.jardimbotanico.backend.model.Credenciais;
import com.jardimbotanico.backend.repository.CredenciaisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class CredenciaisControllerTest {

    private MockMvc mockMvc;

    @Mock  // Mock das credenciais
    private CredenciaisRepository credenciaisRepository;

    @InjectMocks
    private CredenciaisController credenciaisController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Inicia os mocks
        mockMvc = MockMvcBuilders.standaloneSetup(credenciaisController).build(); // Configura o MockMvc
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // Criar dados de credenciais válidas
        Credenciais credenciais = new Credenciais();
        credenciais.setUsername("usuario");
        credenciais.setPassword("senhaSegura");

        // Mock do comportamento do repositório
        when(credenciaisRepository.findByUsername("usuario")).thenReturn(Optional.of(credenciais));

        // Enviar uma requisição POST com dados válidos
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content("{\"username\":\"usuario\",\"password\":\"senhaSegura\"}"))
                .andExpect(status().isOk()) // Espera status HTTP 200 (OK)
                .andExpect(content().string("Login bem-sucedido")) // Espera o conteúdo da resposta
                .andReturn();
    }

    @Test
    public void testLoginFailureInvalidPassword() throws Exception {
        // Criar dados de credenciais
        Credenciais credenciais = new Credenciais();
        credenciais.setUsername("usuario");
        credenciais.setPassword("senhaSegura");

        // Mock do comportamento do repositório
        when(credenciaisRepository.findByUsername("usuario")).thenReturn(Optional.of(credenciais));

        // Enviar uma requisição POST com senha errada
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content("{\"username\":\"usuario\",\"password\":\"senhaErrada\"}"))
                .andExpect(status().isUnauthorized()) // Espera status HTTP 401 (Unauthorized)
                .andExpect(content().string("Credenciais inválidas")); // Espera a mensagem de falha no login
    }

    @Test
    public void testLoginFailureUserNotFound() throws Exception {
        // Mock do comportamento do repositório para não encontrar usuário
        when(credenciaisRepository.findByUsername("usuarioInvalido")).thenReturn(Optional.empty());

        // Enviar uma requisição POST com usuário inexistente
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content("{\"username\":\"usuarioInvalido\",\"password\":\"senhaSegura\"}"))
                .andExpect(status().isUnauthorized()) // Espera status HTTP 401 (Unauthorized)
                .andExpect(content().string("Credenciais inválidas")); // Espera a mensagem de falha no login
    }
}
