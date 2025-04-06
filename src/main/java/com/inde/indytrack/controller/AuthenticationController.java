package com.inde.indytrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.inde.indytrack.repository.StudentRepository;
import com.inde.indytrack.repository.AdminRepository;
import com.inde.indytrack.dto.LoginDTO;
import com.inde.indytrack.dto.RegisterDTO;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.model.Admin;
import com.inde.indytrack.model.Student;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@RequestBody RegisterDTO newStudent) {
        if (studentRepository.existsByEmail(newStudent.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        String hashedPassword = passwordEncoder.encode(newStudent.getPassword());

        Student student = new Student();
        student.setFirstName(newStudent.getFirstName());
        student.setLastName(newStudent.getLastName());
        student.setEmail(newStudent.getEmail());
        student.setPassword(hashedPassword);
        studentRepository.save(student);
        return ResponseEntity.ok("Student has been registered successfully.");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterDTO newAdmin) {
        if (adminRepository.existsByEmail(newAdmin.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        String hashedPassword = passwordEncoder.encode(newAdmin.getPassword());

        Admin admin = new Admin();
        admin.setFirstName(newAdmin.getFirstName());
        admin.setLastName(newAdmin.getLastName());
        admin.setEmail(newAdmin.getEmail());
        admin.setPassword(hashedPassword);
        adminRepository.save(admin);
        return ResponseEntity.ok("Admin has been registered successfully.");
    }

    @PostMapping("/login/student")
    public Student studentLogin(@RequestBody LoginDTO loginDto) {
        Student student = studentRepository.findByEmail(loginDto.getEmail());
        if (student == null) {
            throw new StudentNotFoundException(loginDto.getEmail());
        }

        boolean passwordMatches = passwordEncoder.matches(loginDto.getPassword(), student.getPassword()) || 
            loginDto.getPassword().equals(student.getPassword());

        if (!passwordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return student;
    }

    @PostMapping("/login/admin")
    public Admin adminLogin(@RequestBody LoginDTO loginDto) {
        Admin admin = adminRepository.findByEmail(loginDto.getEmail());
        if (admin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin with email " + loginDto.getEmail() + " not found");
        }

        boolean passwordMatches = passwordEncoder.matches(loginDto.getPassword(), admin.getPassword()) || 
            loginDto.getPassword().equals(admin.getPassword());

        if (!passwordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return admin;
    }

}
