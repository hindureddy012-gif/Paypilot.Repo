package com.example.paypilot.otpservice;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private String generatedOtp;

    public void sendOtp(String email) {

        Random random = new Random();

        generatedOtp = String.valueOf(
                100000 + random.nextInt(900000));

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom("hindureddy012@gmail.com");
        message.setTo(email);
        message.setSubject("PayPilot OTP Verification");
        message.setText("Your OTP is : " + generatedOtp);

        mailSender.send(message);
    }

    public boolean verifyOtp(String otp) {

        return generatedOtp != null
                && generatedOtp.equals(otp);
    }
}