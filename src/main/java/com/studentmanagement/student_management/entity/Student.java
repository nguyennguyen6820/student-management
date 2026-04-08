package com.studentmanagement.student_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "student")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Column(name = "gender")
    private Boolean gender;

    @ManyToOne // Nhiều sinh viên thuộc về một lớp
    @JoinColumn(name = "class_id") // Tên cột khóa ngoại trong bảng student
    private Classroom classroom;
}
