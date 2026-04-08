package com.studentmanagement.student_management.repository;

import com.studentmanagement.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Tìm kiếm với phần trang
    Page<Student> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String fullName, String email,
            Pageable pageable);

    // Lấy 5 sinh viên mới nhất
    List<Student> findTop5ByOrderByIdDesc();

    // Lấy thống kê số lượng sinh viên theo từng khoa cho biểu đồ
    @Query("SELECT d.name, COUNT(s) FROM Student s JOIN s.classroom c JOIN c.department d GROUP BY d.id, d.name")
    List<Object[]> countStudentsByDepartment();

}