package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inde.indytrack.entity.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "select * from students s " +
            "where lower(s.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(s.lastName) like lower(concat('%', :searchTerm, '%'))", nativeQuery = true)
    List<Student> search(@Param("searchTerm") String searchTerm);

    @Query(value = "select * from students s " +
            "where s.id = :searchTerm", nativeQuery = true)
    List<Student> findStudentById(@Param("searchTerm") Long searchTerm);
}
