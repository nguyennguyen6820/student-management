package com.studentmanagement.student_management.controller;

import com.studentmanagement.student_management.entity.Classroom;
import com.studentmanagement.student_management.repository.ClassroomRepository;
import com.studentmanagement.student_management.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/classes")
public class ClassroomController {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public String listClasses(Model model) {
        model.addAttribute("classrooms", classroomRepository.findAll());
        return "class/list";
    }

    @GetMapping("/new")
    public String newClassForm(Model model) {
        model.addAttribute("classroom", new Classroom());
        model.addAttribute("departments", departmentRepository.findAll());
        return "class/form";
    }

    @GetMapping("/edit/{id}")
    public String editClassForm(@PathVariable Integer id, Model model) {
        model.addAttribute("classroom", classroomRepository.findById(id).orElse(new Classroom()));
        model.addAttribute("departments", departmentRepository.findAll());
        return "class/form";
    }

    @PostMapping("/save")
    public String saveClass(@ModelAttribute("classroom") Classroom classroom) {
        classroomRepository.save(classroom);
        return "redirect:/classes";
    }

    @GetMapping("/delete/{id}")
    public String deleteClass(@PathVariable Integer id) {
        classroomRepository.deleteById(id);
        return "redirect:/classes";
    }
}
