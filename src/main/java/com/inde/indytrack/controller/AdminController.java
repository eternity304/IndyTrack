package com.inde.indytrack.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.inde.indytrack.repository.AdminRepository;

@RestController
public class AdminController {

    @Autowired
    private final AdminRepository adminRepository;

    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Admin";
    }
}