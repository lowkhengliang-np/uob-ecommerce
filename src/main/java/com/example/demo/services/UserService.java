package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.User;
import com.example.demo.repo.UserRepo;

//When java see that a class is marked as service, when the springboot application starts,
//Springboot will register this class as a service and make it available to any other controllers.
@Service
public class UserService implements UserDetailsService{
    // The spring framework security will auto use userdetail service to retrieve info about user.
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User findUserByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public User registerNewUser(User user){
        // before we save the user to the database, we have to hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        System.out.println("User = " + user.getUsername());
        System.out.println("User = " + user.getPassword());
        if (user==null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    
}
