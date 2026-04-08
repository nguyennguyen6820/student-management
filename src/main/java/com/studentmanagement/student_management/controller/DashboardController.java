package com.studentmanagement.student_management.controller;

import com.studentmanagement.student_management.repository.DepartmentRepository;
import com.studentmanagement.student_management.repository.InstructorRepository;
import com.studentmanagement.student_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private com.studentmanagement.student_management.repository.EnrollmentRepository enrollmentRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        long totalStudents = studentRepository.count();
        long totalDepartments = departmentRepository.count();
        long totalInstructors = instructorRepository.count();
        
        long goodGrades = enrollmentRepository.findAll().stream().filter(e -> e.getTotalGrade() != null && e.getTotalGrade() >= 8.0).count();
        long fairGrades = enrollmentRepository.findAll().stream().filter(e -> e.getTotalGrade() != null && e.getTotalGrade() >= 6.5 && e.getTotalGrade() < 8.0).count();
        long averageGrades = enrollmentRepository.findAll().stream().filter(e -> e.getTotalGrade() != null && e.getTotalGrade() >= 5.0 && e.getTotalGrade() < 6.5).count();
        long poorGrades = enrollmentRepository.findAll().stream().filter(e -> e.getTotalGrade() != null && e.getTotalGrade() < 5.0).count();
        
        java.util.List<Long> gradeStats = java.util.Arrays.asList(goodGrades, fairGrades, averageGrades, poorGrades);

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalDepartments", totalDepartments);
        model.addAttribute("totalInstructors", totalInstructors);

        model.addAttribute("recentStudents", studentRepository.findTop5ByOrderByIdDesc());
        model.addAttribute("chartData", studentRepository.countStudentsByDepartment());
        model.addAttribute("gradeStats", gradeStats);

        return "index"; 
    }
}
