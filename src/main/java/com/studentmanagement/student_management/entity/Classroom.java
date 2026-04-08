package com.studentmanagement.student_management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "classroom")
@Data
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; // Tên lớp (ví dụ: CNTT1, Kế Toán 2)

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;
}