package com.studentmanagement.student_management.service;

import com.studentmanagement.student_management.entity.Enrollment;
import com.studentmanagement.student_management.entity.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportStudentsToExcel(List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Danh_sach_Sinh_vien");
            
            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Họ Tên", "Email", "Ngày Sinh", "Giới Tính", "Lớp Học"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            
            // Data
            int rowIdx = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getFullName() != null ? student.getFullName() : "");
                row.createCell(2).setCellValue(student.getEmail() != null ? student.getEmail() : "");
                row.createCell(3).setCellValue(student.getDob() != null ? student.getDob().toString() : "");
                row.createCell(4).setCellValue(student.getGender() != null ? (student.getGender() ? "Nam" : "Nữ") : "");
                row.createCell(5).setCellValue(student.getClassroom() != null ? student.getClassroom().getName() : "");
            }
            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xuất file Excel", e);
        }
    }

    public ByteArrayInputStream exportGradesToExcel(List<Enrollment> enrollments, String className, String subjectName) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bang_Diem");
            
            // Info Row
            Row infoRow = sheet.createRow(0);
            infoRow.createCell(0).setCellValue("Lớp:");
            infoRow.createCell(1).setCellValue(className);
            infoRow.createCell(3).setCellValue("Môn học:");
            infoRow.createCell(4).setCellValue(subjectName);
            
            // Header
            Row headerRow = sheet.createRow(2);
            String[] columns = {"Mã SV", "Họ Tên", "Điểm CC (10%)", "Điểm GK (30%)", "Điểm CK (60%)", "Tổng Kết"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            
            // Data
            int rowIdx = 3;
            for (Enrollment en : enrollments) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(en.getStudent().getId());
                row.createCell(1).setCellValue(en.getStudent().getFullName());
                
                if (en.getAttendanceGrade() != null) row.createCell(2).setCellValue(en.getAttendanceGrade());
                if (en.getMidtermGrade() != null) row.createCell(3).setCellValue(en.getMidtermGrade());
                if (en.getFinalGrade() != null) row.createCell(4).setCellValue(en.getFinalGrade());
                
                if (en.getTotalGrade() != null) {
                    row.createCell(5).setCellValue(en.getTotalGrade());
                } else if (en.getAttendanceGrade() != null && en.getMidtermGrade() != null && en.getFinalGrade() != null) {
                    // Fallback tính dẹp
                    double t = en.getAttendanceGrade() * 0.1 + en.getMidtermGrade() * 0.3 + en.getFinalGrade() * 0.6;
                    row.createCell(5).setCellValue(Math.round(t * 10.0) / 10.0);
                }
            }
            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xuất file bảng điểm Excel", e);
        }
    }
}
