package com.studentmanagement.student_management.controller;

import com.studentmanagement.student_management.entity.Enrollment;
import com.studentmanagement.student_management.repository.ClassroomRepository;
import com.studentmanagement.student_management.repository.EnrollmentRepository;
import com.studentmanagement.student_management.repository.SubjectRepository;
import com.studentmanagement.student_management.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping
    public String listGrades(
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) Integer subjectId,
            Model model) {
        
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        
        if (classId != null && subjectId != null) {
            List<Enrollment> enrollments = enrollmentRepository.findBySubjectIdAndStudentClassroomId(subjectId, classId);
            
            // Lọc loại bỏ Enrollment bị trùng lặp do bấm đúp đăng ký môn
            java.util.Map<Integer, Enrollment> uniqueEnrollments = new java.util.HashMap<>();
            java.util.List<Enrollment> toDelete = new java.util.ArrayList<>();
            for (Enrollment en : enrollments) {
                if (uniqueEnrollments.containsKey(en.getStudent().getId())) {
                    toDelete.add(en); // Lưu lại để xóa ở database nếu cần (Tùy chọn)
                } else {
                    uniqueEnrollments.put(en.getStudent().getId(), en);
                }
            }
            if (!toDelete.isEmpty()) {
                enrollmentRepository.deleteAll(toDelete);
            }
            java.util.List<Enrollment> distinctEnrollments = new java.util.ArrayList<>(uniqueEnrollments.values());
            
            model.addAttribute("enrollments", distinctEnrollments);
            
            model.addAttribute("selectedClass", classroomRepository.findById(classId).orElse(null));
            model.addAttribute("selectedSubject", subjectRepository.findById(subjectId).orElse(null));
        }

        return "grade/list";
    }

    @Autowired
    private GradeService gradeService;

    @PostMapping("/save")
    public String saveGrades(
        @RequestParam Integer classId, 
        @RequestParam Integer subjectId,
        @RequestParam Map<String, String> requestParams) {
        
        List<Enrollment> enrollments = enrollmentRepository.findBySubjectIdAndStudentClassroomId(subjectId, classId);
        
        gradeService.processAndSaveGrades(enrollments, requestParams);
        
        return "redirect:/grades?classId=" + classId + "&subjectId=" + subjectId;
    }

    @Autowired
    private com.studentmanagement.student_management.service.ExcelExportService excelService;

    @GetMapping("/export")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.InputStreamResource> exportGrades(
            @RequestParam Integer classId,
            @RequestParam Integer subjectId) {

        List<Enrollment> enrollments = enrollmentRepository.findBySubjectIdAndStudentClassroomId(subjectId, classId);
        String className = classroomRepository.findById(classId).map(c -> c.getName()).orElse("Unknown");
        String subjectName = subjectRepository.findById(subjectId).map(s -> s.getName()).orElse("Unknown");
        
        java.io.ByteArrayInputStream in = excelService.exportGradesToExcel(enrollments, className, subjectName);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=BangDiem_" + className + "_" + subjectName + ".xlsx");

        return org.springframework.http.ResponseEntity
                .ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new org.springframework.core.io.InputStreamResource(in));
    }

    @Autowired
    private com.studentmanagement.student_management.repository.StudentRepository studentRepository;

    @GetMapping("/student/{id}")
    public String showStudentTranscript(@PathVariable Integer id, Model model) {
        com.studentmanagement.student_management.entity.Student student = studentRepository.findById(id).orElse(null);
        if (student == null) return "redirect:/students";
        
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(id);
        
        double totalSum = 0;
        int totalCredits = 0;
        
        for (Enrollment en : enrollments) {
            if (en.getTotalGrade() != null) {
                totalSum += en.getTotalGrade() * en.getSubject().getCredits();
                totalCredits += en.getSubject().getCredits();
            }
        }
        
        Double gpa = totalCredits > 0 ? (totalSum / totalCredits) : null;
        
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("gpa", gpa);
        model.addAttribute("student", student);
        
        return "grade/student_detail";
    }
}
