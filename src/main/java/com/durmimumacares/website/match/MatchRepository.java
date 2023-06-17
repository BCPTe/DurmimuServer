package com.durmimumacares.website.match;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends MongoRepository<Match, ObjectId> {
    Optional<List<Match>> findMatchByDatetime(Long datetime);

}
