package com.jardimbotanico.backend.repository;

import com.jardimbotanico.backend.model.Credenciais;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CredenciaisRepository extends MongoRepository<Credenciais, String> {
    Optional<Credenciais> findByUsername(String username);
}
