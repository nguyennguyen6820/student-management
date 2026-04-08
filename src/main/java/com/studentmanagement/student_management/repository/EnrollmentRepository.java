package com.studentmanagement.student_management.repository;

import com.studentmanagement.student_management.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findBySubjectIdAndStudentClassroomId(Integer subjectId, Integer classroomId);
    
    List<Enrollment> findByStudentId(Integer studentId);
}
