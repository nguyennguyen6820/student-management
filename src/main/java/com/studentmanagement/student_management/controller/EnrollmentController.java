package com.studentmanagement.student_management.controller;

import com.studentmanagement.student_management.entity.Enrollment;
import com.studentmanagement.student_management.entity.Student;
import com.studentmanagement.student_management.repository.EnrollmentRepository;
import com.studentmanagement.student_management.repository.StudentRepository;
import com.studentmanagement.student_management.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping("/register/{studentId}")
    public String registerSubjectForm(@PathVariable Integer studentId, Model model) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return "redirect:/students";
        
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentId(studentId);
        List<Integer> enrolledSubjectIds = existingEnrollments.stream()
            .map(e -> e.getSubject().getId())
            .collect(Collectors.toList());
            
        model.addAttribute("student", student);
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("enrolledSubjectIds", enrolledSubjectIds);
        
        return "enrollment/register";
    }

    @PostMapping("/save/{studentId}")
    public String saveEnrollments(@PathVariable Integer studentId, 
                                  @RequestParam(required = false) List<Integer> subjectIds) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return "redirect:/students";
        
        List<Enrollment> existing = enrollmentRepository.findByStudentId(studentId);
        List<Integer> existingIds = existing.stream()
            .map(e -> e.getSubject().getId())
            .collect(Collectors.toList());
        
        if (subjectIds == null) subjectIds = new ArrayList<>();
        
        for (Enrollment en : existing) {
            if (!subjectIds.contains(en.getSubject().getId())) {
                enrollmentRepository.delete(en);
            }
        }
        
        for (Integer subId : subjectIds) {
            if (!existingIds.contains(subId)) {
                Enrollment newEn = new Enrollment();
                newEn.setStudent(student);
                newEn.setSubject(subjectRepository.findById(subId).orElse(null));
                enrollmentRepository.save(newEn);
            }
        }
        
        return "redirect:/students";
    }
}
