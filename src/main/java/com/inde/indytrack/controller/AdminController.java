package com.inde.indytrack.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inde.indytrack.repository.AdminRepository;
import com.inde.indytrack.dto.RegisterDTO;
import com.inde.indytrack.exception.AdminNotFoundException;
import com.inde.indytrack.model.Admin;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private final AdminRepository repository;

    public AdminController(AdminRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Admin> retrieveAllAdmins() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Admin retrieveAdmin(@PathVariable("id") Long adminId) {
        return repository.findById(adminId)
            .orElseThrow(() -> new AdminNotFoundException(adminId));
    }

    @PostMapping
    public Admin createAdmin(@RequestBody RegisterDTO newAdmin) {
        if (repository.existsByEmail(newAdmin.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        Admin admin = new Admin();
        admin.setFirstName(newAdmin.getFirstName());
        admin.setLastName(newAdmin.getLastName());
        admin.setEmail(newAdmin.getEmail());
        admin.setPassword(newAdmin.getPassword());
        return repository.save(admin);
    }

    @PutMapping("/{id}")
    public Admin updateAdmin(@PathVariable("id") Long adminId, @RequestBody Admin newAdmin) {
        return repository.findById(adminId)
                .map(existingAdmin -> {
                    existingAdmin.setFirstName(newAdmin.getFirstName());
                    existingAdmin.setLastName(newAdmin.getLastName());
                    existingAdmin.setEmail(newAdmin.getEmail());
                    existingAdmin.setPassword(newAdmin.getPassword());
                    return repository.save(existingAdmin);
                })
                .orElseThrow(() -> new AdminNotFoundException(adminId));
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable("id") Long adminId) {
        Admin admin = repository.findById(adminId)
            .orElseThrow(() -> new AdminNotFoundException(adminId));
        repository.delete(admin);
    }

}