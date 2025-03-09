package com.inde.indytrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.inde.indytrack.entity.Admin;
import com.inde.indytrack.entity.Student;
import com.inde.indytrack.repository.StudentRepository;
import com.inde.indytrack.repository.AdminRepository;
import com.inde.indytrack.dto.LoginDTO;
import com.inde.indytrack.dto.RegisterDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@RequestBody RegisterDTO newStudent) {
        if (studentRepository.findByEmail(newStudent.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        // TODO: Hash password
        // String hashedPassword = passwordEncoder.encode(newStudent.getPassword());

        Student student = new Student();
        student.setFirstName(newStudent.getFirstName());
        student.setLastName(newStudent.getLastName());
        student.setEmail(newStudent.getEmail());
        student.setPassword(newStudent.getPassword());
        studentRepository.save(student);
        return ResponseEntity.ok("Student has been registered successfully.");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterDTO newAdmin) {
        if (adminRepository.findByEmail(newAdmin.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        // TODO: Hash password
        // String hashedPassword = passwordEncoder.encode(newAdmin.getPassword());

        Admin admin = new Admin();
        admin.setFirstName(newAdmin.getFirstName());
        admin.setLastName(newAdmin.getLastName());
        admin.setEmail(newAdmin.getEmail());
        admin.setPassword(newAdmin.getPassword());
        adminRepository.save(admin);
        return ResponseEntity.ok("Admin has been registered successfully.");
    }

    @PostMapping("/login/student")
    public Student studentLogin(@RequestBody LoginDTO loginDto) {
        Student student = studentRepository.findByEmail(loginDto.getEmail());
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with email " + loginDto.getEmail() + " not found");
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), student.getPassword())) {
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

        if (!passwordEncoder.matches(loginDto.getPassword(), admin.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return admin;
    }

}
