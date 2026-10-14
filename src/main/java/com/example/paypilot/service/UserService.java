package com.example.paypilot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paypilot.entity.User;
import com.example.paypilot.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE USER
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // GET ALL USERS
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET USER BY ID
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    // UPDATE USER
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // DELETE USER
    public String deleteUser(int id) {
        userRepository.deleteById(id);
        return "User Deleted Successfully";
    }

    // LOGIN WITH ACCOUNT LOCK FEATURE
    public String login(int userId, String password) {

        User user = userRepository.findByUserId(userId)
                .orElse(null);

        if (user == null) {
            return "User Not Found";
        }

        if (Boolean.TRUE.equals(user.getAccountLocked())) {
            return "Account Locked";
        }

        if (user.getPassword().equals(password)) {

            user.setLoginAttempts(0);

            userRepository.save(user);

            return "Login Successful";
        }

        int attempts = user.getLoginAttempts() == null
                ? 0
                : user.getLoginAttempts();

        attempts++;

        user.setLoginAttempts(attempts);

        if (attempts >= 3) {

            user.setAccountLocked(true);

            userRepository.save(user);

            return "Account Locked";
        }

        userRepository.save(user);

        return "Invalid Password. Attempts Left : "
                + (3 - attempts);
    }

    // RESET PASSWORD
    public String resetPassword(int userId, String newPassword) {

        User user = userRepository.findByUserId(userId)
                .orElse(null);

        if (user == null) {
            return "User Not Found";
        }

        user.setPassword(newPassword);

        userRepository.save(user);

        return "Password Updated Successfully";
    }
}