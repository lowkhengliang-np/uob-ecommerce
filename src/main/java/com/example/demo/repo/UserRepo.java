package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    // automated query generation
    // will convert based on the method name to select * from user_accounts where username = ?
    User findByUsername(String username);
}
