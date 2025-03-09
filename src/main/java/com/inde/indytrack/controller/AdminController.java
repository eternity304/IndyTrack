package com.inde.indytrack.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inde.indytrack.repository.AdminRepository;
import com.inde.indytrack.entity.Admin;
import com.inde.indytrack.exception.AdminNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private final AdminRepository adminRepository;

    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping
    public List<Admin> getAdmin() {
        return adminRepository.findAll();
    }

    @GetMapping("/{id}")
    public Admin retrieveAdmin(@PathVariable("id") Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));
    }

    @PutMapping("/{id}")
    public Admin updateAdmin(@PathVariable("id") Long adminId, @RequestBody Admin newAdmin) {
        return adminRepository.findById(adminId)
                .map(existingAdmin -> {
                    existingAdmin.setFirstName(newAdmin.getFirstName());
                    existingAdmin.setLastName(newAdmin.getLastName());
                    existingAdmin.setEmail(newAdmin.getEmail());
                    existingAdmin.setPassword(newAdmin.getPassword());
                    return adminRepository.save(existingAdmin);
                })
                .orElseGet(() -> {
                    newAdmin.setId(adminId);
                    return adminRepository.save(newAdmin);
                });
    }

    @DeleteMapping("/{id}")
    public String deleteAdmin(@PathVariable("id") Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new AdminNotFoundException(adminId);
        }
        adminRepository.deleteById(adminId);
        return "Admin with ID " + adminId + " has been deleted successfully";
    }

}