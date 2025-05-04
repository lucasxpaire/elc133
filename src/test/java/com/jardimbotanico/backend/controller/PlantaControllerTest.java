package com.jardimbotanico.backend.controller;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import com.jayway.jsonpath.Criteria;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
// import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

// import com.jardimbotanico.backend.Controller.PlantaController;

public class PlantaControllerTest {

  @Mock
  private MongoTemplate mongoTemplate;

  @InjectMocks
  private PlantaController plantaController;

  private static final String COLLECTION_NAME = "plantas";

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void deveCadastrarPlanta() throws Exception {
    // Simula uma planta sendo enviada via JSON
    Map<String, Object> plantaMock = Map.of("nome", "samambaia", "classe", "Psliotopsida");
    when(mongoTemplate.insert(plantaMock, COLLECTION_NAME)).thenReturn(plantaMock);

    var response = plantaController.cadastrarPlanta(plantaMock);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("samambaia", ((Map) response.getBody()).get("nome"));
  }
  
  @Test
  void deveListarPlantas() throws Exception {
    List<Map> plantasMock = List.of(
        Map.of("nome", "Cacto"),
        Map.of("nome", "Samambaia"));

    when(mongoTemplate.findAll(Map.class, COLLECTION_NAME)).thenReturn(plantasMock);
    List<Map> resultado = plantaController.listarPlantas();

    assertEquals(2, resultado.size());
    assertEquals("Cacto", resultado.get(0).get("nome"));
    assertEquals("Samambaia", resultado.get(1).get("nome"));
  }

  @Test
  void deveRetornarListaVazia() {
    when(mongoTemplate.findAll(Map.class, COLLECTION_NAME)).thenReturn(List.of());
    List<Map> resultado = plantaController.listarPlantas();
    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
  }

  @Test
  void deveRetornarUmaPlanta() {
    String id = "123";
    Map<String, Object> plantaMock = Map.of(
        "_id", id,
        "nome", "samambaia",
        "classe", "Psliotopsida");

    // Configura o mock do mongoTemplate
    when(mongoTemplate.findOne(
        any(Query.class),
        eq(Map.class),
        eq("plantas")))
        .thenReturn(plantaMock);

    ResponseEntity<?> response = plantaController.buscarUmaPlanta(id);

    // Verificações
    assertEquals(HttpStatus.OK, response.getStatusCode());

    Map<String, Object> resultado = (Map<String, Object>) response.getBody();
    assertNotNull(resultado);
    assertEquals(id, resultado.get("_id"));
    assertEquals("samambaia", resultado.get("nome"));
    assertEquals("Psliotopsida", resultado.get("classe"));
  }
  
}
