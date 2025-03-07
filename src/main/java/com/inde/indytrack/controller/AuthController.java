package com.inde.indytrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.inde.indytrack.entity.Admin;
import com.inde.indytrack.entity.Student;
import com.inde.indytrack.entity.User;
import com.inde.indytrack.entity.VerificationToken;
import com.inde.indytrack.repository.StudentRepository;
import com.inde.indytrack.repository.UserRepository;
import com.inde.indytrack.repository.AdminRepository;
import com.inde.indytrack.repository.VerificationTokenRepository;
import com.inde.indytrack.service.EmailService;
import com.inde.indytrack.dto.LoginDTO;
import com.inde.indytrack.dto.UserDTO;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        
        User user;
        if (userDto.getRole().equalsIgnoreCase("student")) {
            user = new Student();
        } else if (userDto.getRole().equalsIgnoreCase("admin")) {
            user = new Admin();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user type");
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(hashedPassword);
        user.setVerified(false);

        // Save the user
        user = userRepository.save(user);
        
        // Generate a verification token
        VerificationToken verificationToken= new VerificationToken(user);
        tokenRepository.save(verificationToken);

        // Send a verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken.getToken());

        return ResponseEntity.ok("User has been registered successfully. Please verify your email.");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationTokenOptional = tokenRepository.findByToken(token);
        if (verificationTokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token");
        }

        VerificationToken verificationToken = verificationTokenOptional.get();

        if (verificationToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        // Delete the verification token after its use
        tokenRepository.delete(verificationToken);

        return ResponseEntity.ok("Email has been verified successfully. You can now log in.");
    }
    @PostMapping("/login")
    public User login(@RequestBody LoginDTO loginDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        User user = userOptional.get();
        
        if (!user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account has not been verified yet. Please check your email inbox for verification.");
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return user;
    }

}
