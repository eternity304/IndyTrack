package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.entity.Admin;
import com.inde.indytrack.entity.Comment;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query(value = "select * from comment c " +
            "where lower(c.courseId) like lower(concat('%', :searchTerm, '%')) ", nativeQuery = true)
    List<Comment> findCommentById(@Param("searchTerm") String searchTerm);
}
