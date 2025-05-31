package com.lukrlier.notabene.repository;

import com.lukrlier.notabene.domain.Authority;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {}
