package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inde.indytrack.model.Minor;
import com.inde.indytrack.model.MinorRequirement;

@Repository
public interface MinorRepository extends JpaRepository<Minor, String> {
    @Query(
        value = "SELECT * FROM minors WHERE LOWER(name) LIKE LOWER(:searchName)", 
        nativeQuery = true
    )
    Minor findByName(@Param("searchName") String searchName);

    boolean existsByName(String name);
    
    @Query(
        value = "SELECT * FROM minor_requirements WHERE minor_name = :minorName",
        nativeQuery = true
    )
    List<MinorRequirement> findRequirementsByMinorName(@Param("minorName") String minorName);

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM minor_requirements WHERE minor_name = :minorName",
        nativeQuery = true
    )
    void deleteRequirementsByMinorName(@Param("minorName") String minorName);

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM minors WHERE LOWER(name) LIKE LOWER(:minorName)",
        nativeQuery = true
    )
    void deleteByName(@Param("minorName") String minorName);

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM student_intended_minors WHERE minor_name = :minorName",
        nativeQuery = true
    )
    void deleteStudentIntendedMinors(@Param("minorName") String minorName);
}
