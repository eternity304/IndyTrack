package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.model.Comment;
import com.inde.indytrack.model.CommentKey;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentKey> {
    public List<Comment> findByIdCourseCode(String courseCode);
    public List<Comment> findByIdStudentId(Long studentId);
    public List<Comment> findByIdStudentIdAndIdCourseCode(Long studentId, String courseCode);

    @Query(
        value = "SELECT * FROM comments c " + 
                "WHERE c.course_code = :courseCode " + 
                "ORDER BY c.upload_time DESC",
        nativeQuery = true
    ) public List<Comment> findLatestCommentsByCourseCode(@Param("courseCode") String courseCode);
}
