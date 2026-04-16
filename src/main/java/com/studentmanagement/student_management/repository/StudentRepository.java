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
    @Query(value = "SELECT * FROM student WHERE LOWER(full_name) LIKE LOWER(CONCAT('%', :fullName, '%')) OR LOWER(email) LIKE LOWER(CONCAT('%', :email, '%'))",
           countQuery = "SELECT COUNT(*) FROM student WHERE LOWER(full_name) LIKE LOWER(CONCAT('%', :fullName, '%')) OR LOWER(email) LIKE LOWER(CONCAT('%', :email, '%'))",
           nativeQuery = true)
    Page<Student> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@org.springframework.data.repository.query.Param("fullName") String fullName, @org.springframework.data.repository.query.Param("email") String email,
            Pageable pageable);

    // Lấy 5 sinh viên mới nhất
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findTop5ByOrderByIdDesc();

    // Lấy thống kê số lượng sinh viên theo từng khoa cho biểu đồ
    @Query(value = "SELECT d.name, COUNT(s.id) FROM student s JOIN classroom c ON s.class_id = c.id JOIN department d ON c.dept_id = d.id GROUP BY d.id, d.name", nativeQuery = true)
    List<Object[]> countStudentsByDepartment();

    @Query(value = "SELECT s.* FROM student s JOIN app_users u ON s.user_id = u.id WHERE u.username = :username", nativeQuery = true)
    Student findByUserUsername(@org.springframework.data.repository.query.Param("username") String username);
}