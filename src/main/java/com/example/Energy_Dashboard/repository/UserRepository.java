package com.example.Energy_Dashboard.repository;

import com.example.Energy_Dashboard.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmailId(String emailId);
}
