package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select * from comments c " +
            "where lower(c.courseCode) like lower(concat('%', :searchTerm, '%')) ", nativeQuery = true)
    List<Comment> findCommentByCourseCode(@Param("searchTerm") String searchTerm);

    List<Comment> findCommentByStudentId(@Param("searchTerm") Long studentId);

}
