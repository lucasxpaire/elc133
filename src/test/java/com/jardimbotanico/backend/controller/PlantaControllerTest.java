package com.jardimbotanico.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import com.jayway.jsonpath.Criteria;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@SuppressWarnings("unchecked")
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
    assertEquals("samambaia", ((Map<String, Object>) response.getBody()).get("nome"));
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
      Map<String, Object> plantaMock = new HashMap<>();
      plantaMock.put("_id", id);
      plantaMock.put("nome", "samambaia");
      plantaMock.put("classe", "Psliotopsida");
  
      // Configura o mock do mongoTemplate com argThat
      when(mongoTemplate.findOne(
          argThat(query -> query.getQueryObject().get("_id").equals(id)),
          eq(Map.class),
          eq(COLLECTION_NAME))
      ).thenReturn(plantaMock);
  
      ResponseEntity<?> response = plantaController.buscarUmaPlanta(id);
  
      // Verificações
      assertEquals(HttpStatus.OK, response.getStatusCode());
      Map<String, Object> resultado = (Map<String, Object>) response.getBody();
      assertNotNull(resultado);
      assertEquals(id, resultado.get("_id")); // aqui está ok pois o controller converteu pra string
      assertEquals("samambaia", resultado.get("nome"));
      assertEquals("Psliotopsida", resultado.get("classe"));
  }
  

  @Test
  void deveAtualizarPlantaComSucesso() {
    String id = "123";
    Map<String, Object> plantaAtualizada = Map.of(
        "nome", "samambaia-atualizada",
        "classe", "Psilotopsida");

    // Simula o resultado da atualização
    UpdateResult updateResultMock = mock(UpdateResult.class);
    when(updateResultMock.getModifiedCount()).thenReturn(1L);

    // Simula o comportamento do mongoTemplate
    when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(COLLECTION_NAME)))
        .thenReturn(updateResultMock);

    // Executa o método do controller
    ResponseEntity<Void> response = plantaController.atualizarPlanta(id, plantaAtualizada);

    // Verifica se o status é 200 OK
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void deveRetornarNotFoundSeNadaForAtualizado() {
    String id = "999";
    Map<String, Object> plantaAtualizada = Map.of(
        "nome", "planta-inexistente");

    // Simula que nenhuma planta foi atualizada
    UpdateResult updateResultMock = mock(UpdateResult.class);
    when(updateResultMock.getModifiedCount()).thenReturn(0L);

    when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(COLLECTION_NAME)))
        .thenReturn(updateResultMock);

    ResponseEntity<Void> response = plantaController.atualizarPlanta(id, plantaAtualizada);

    // Verifica se o status é 404 Not Found
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void deveDeletarPlantaComSucesso() {
    String id = "123";

    // Simula um resultado com uma exclusão realizada
    DeleteResult deleteResultMock = mock(DeleteResult.class);
    when(deleteResultMock.getDeletedCount()).thenReturn(1L);

    when(mongoTemplate.remove(any(Query.class), eq(COLLECTION_NAME)))
        .thenReturn(deleteResultMock);

    ResponseEntity<Void> response = plantaController.deletarPlanta(id);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void deveRetornarNotFoundSeNadaForDeletado() {
    String id = "123";

    DeleteResult deleteResultMock = mock(DeleteResult.class);
    when(deleteResultMock.getDeletedCount()).thenReturn(0L);
    when(mongoTemplate.remove(any(Query.class), eq(COLLECTION_NAME))).thenReturn(deleteResultMock);

    ResponseEntity<Void> response = plantaController.deletarPlanta(id);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

}
