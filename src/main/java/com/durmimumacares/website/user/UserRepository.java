package com.durmimumacares.website.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<List<User>> findUserByName(String name);
    Optional<List<User>> findUserBySurname(String surname);
    Optional<List<User>> findUserByEmail(String email);
}
