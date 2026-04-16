package com.studentmanagement.student_management.repository;

import com.studentmanagement.student_management.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @org.springframework.data.jpa.repository.Query(value = "SELECT e.* FROM enrollment e JOIN student s ON e.student_id = s.id WHERE e.subject_id = :subjectId AND s.class_id = :classroomId", nativeQuery = true)
    List<Enrollment> findBySubjectIdAndStudentClassroomId(@org.springframework.data.repository.query.Param("subjectId") Integer subjectId, @org.springframework.data.repository.query.Param("classroomId") Integer classroomId);
    
    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM enrollment WHERE student_id = :studentId", nativeQuery = true)
    List<Enrollment> findByStudentId(@org.springframework.data.repository.query.Param("studentId") Integer studentId);
}
