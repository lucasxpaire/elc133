package com.jardimbotanico.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plantas")
public class PlantaController {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "plantas";

    @PostMapping
    public ResponseEntity<?> cadastrarPlanta(@RequestBody Map<String, Object> planta) {
        var novaPlanta = mongoTemplate.insert(planta, COLLECTION_NAME);
        return ResponseEntity.ok(novaPlanta);
    }

    @GetMapping
    public List<Map> listarPlantas() {
        return mongoTemplate.findAll(Map.class, COLLECTION_NAME);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUmaPlanta(@PathVariable String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        var planta = mongoTemplate.findOne(query, Map.class, COLLECTION_NAME);

        if (planta != null) {
            return ResponseEntity.ok(planta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarPlanta(@PathVariable String id, @RequestBody Map<String, Object> plantaAtualizada) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();

        plantaAtualizada.forEach(update::set);

        var result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);

        if (result.getModifiedCount() > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar planta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPlanta(@PathVariable String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        var result = mongoTemplate.remove(query, COLLECTION_NAME);

        if (result.getDeletedCount() > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
