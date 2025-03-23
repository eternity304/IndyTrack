package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.model.Minor;

@Repository
public interface MinorRepository extends JpaRepository<Minor, String> {
    @Query(
        value = "SELECT * FROM minors WHERE LOWER(name) = LOWER(:name)",
        nativeQuery = true
    )
    Minor findByName(@Param("name") String name);
}
