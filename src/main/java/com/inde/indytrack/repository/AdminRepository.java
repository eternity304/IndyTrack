package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    public Admin findByEmail(String email);
    public boolean existsByEmail(String email);
}
