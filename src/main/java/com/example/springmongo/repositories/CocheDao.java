package com.example.springmongo.repositories;

import com.example.springmongo.model.Coche;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocheDao extends MongoRepository<Coche, Integer> {
}
