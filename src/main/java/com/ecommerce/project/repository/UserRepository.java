package com.ecommerce.project.repository;

import com.ecommerce.project.model.User;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(@Email(message = "Email should be valid") @Length(min = 6, max = 40) String email);
}
