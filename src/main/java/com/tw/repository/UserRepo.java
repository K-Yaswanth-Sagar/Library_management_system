package com.tw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tw.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);

}
