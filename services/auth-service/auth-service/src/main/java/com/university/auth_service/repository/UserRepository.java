package com.university.auth_service.repository;

import com.university.auth_service.entity.User;
import com.university.auth_service.entity.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
// ton User document


public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
