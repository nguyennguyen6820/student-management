package com.studentmanagement.student_management.service;

import com.studentmanagement.student_management.entity.Enrollment;
import com.studentmanagement.student_management.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradeService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * Parse and stream through request data to calculate and persist total grades
     */
    @Transactional
    public void processAndSaveGrades(List<Enrollment> enrollments, Map<String, String> requestParams) {
        
        // 1. Dùng quy tắc Filter để tìm các Enrollment có dữ liệu và gán điểm
        List<Enrollment> updatedEnrollments = enrollments.stream()
            .map(en -> {
                Integer studentId = en.getStudent().getId();
                try {
                    boolean modified = false;
                    
                    if (isValidInput(requestParams.get("attendance_" + studentId))) {
                        String rawVal = requestParams.get("attendance_" + studentId).split(",")[0].replace(",", ".");
                        en.setAttendanceGrade(Double.valueOf(rawVal));
                        modified = true;
                    }
                    if (isValidInput(requestParams.get("midterm_" + studentId))) {
                        String rawVal = requestParams.get("midterm_" + studentId).split(",")[0].replace(",", ".");
                        en.setMidtermGrade(Double.valueOf(rawVal));
                        modified = true;
                    }
                    if (isValidInput(requestParams.get("final_" + studentId))) {
                        String rawVal = requestParams.get("final_" + studentId).split(",")[0].replace(",", ".");
                        en.setFinalGrade(Double.valueOf(rawVal));
                        modified = true;
                    }
                    
                    // 2. Tính TỔNG KẾT trước khi lưu vào DB nều có thay đổi điểm
                    if (modified) {
                        calculateTotalGrade(en);
                    }
                    
                    return modified ? en : null;
                } catch (NumberFormatException e) {
                    return null;
                }
            })
            .filter(en -> en != null)
            .collect(Collectors.toList());

        // 3. Batch save vào Database
        if (!updatedEnrollments.isEmpty()) {
            enrollmentRepository.saveAll(updatedEnrollments);
        }
    }

    private boolean isValidInput(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private void calculateTotalGrade(Enrollment en) {
        if (en.getAttendanceGrade() != null && en.getMidtermGrade() != null && en.getFinalGrade() != null) {
            double total = (en.getAttendanceGrade() * 0.1) + 
                           (en.getMidtermGrade() * 0.3) + 
                           (en.getFinalGrade() * 0.6);
            en.setTotalGrade(Math.round(total * 10.0) / 10.0); // Làm tròn 1 chữ số thập phân
        }
    }
}
