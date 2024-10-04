package com.example.security_demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security_demo.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    // no need to write the logic or query to find the user by email
    // the name of the method does it here

    Optional<User> findUserByEmail(String email);
    boolean existsByEmail(String email);
}

/*
Method Naming Convention: 
Spring Data JPA parses the method name `findUserByEmail` and interprets it as:

1. find: 
   - The keyword tells Spring Data JPA that it needs to generate a query 
     to retrieve an entity.

2. User: 
   - Refers to the entity you're working with (in this case, the `User` entity).

3. ByEmail: 
   - This specifies the field that the query will be based on (`email` in this case).

Spring Data JPA uses this naming convention to automatically generate the appropriate
query without the need for explicit query logic.
*/
