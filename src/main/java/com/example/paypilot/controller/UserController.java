package com.example.paypilot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.paypilot.dto.ForgotPasswordRequest;
import com.example.paypilot.dto.LoginRequest;
import com.example.paypilot.dto.OtpRequest;
import com.example.paypilot.entity.User;
import com.example.paypilot.otpservice.OtpService;
import com.example.paypilot.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    // CREATE USER
    @PostMapping("/users")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // GET ALL USERS
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET USER BY ID
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // UPDATE USER
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    // DELETE USER
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        return userService.login(
                request.getUserId(),
                request.getPassword());
    }

    // SEND OTP
    @PostMapping("/sendOtp")
    public String sendOtp(@RequestBody OtpRequest request) {

        otpService.sendOtp(request.getEmail());

        return "OTP Sent Successfully";
    }

    // VERIFY OTP
    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestBody OtpRequest request) {

        boolean result = otpService.verifyOtp(request.getOtp());

        if (result) {
            return "OTP Verified Successfully";
        }

        return "Invalid OTP";
    }

    // RESET PASSWORD
    @PostMapping("/resetPassword")
    public String resetPassword(
            @RequestBody ForgotPasswordRequest request) {

        return userService.resetPassword(
                request.getUserId(),
                request.getNewPassword());
    }
}