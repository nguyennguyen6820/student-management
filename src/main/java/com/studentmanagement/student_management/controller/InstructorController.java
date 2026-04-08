package com.studentmanagement.student_management.controller;

import com.studentmanagement.student_management.entity.Instructor;
import com.studentmanagement.student_management.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping
    public String listInstructors(Model model) {
        model.addAttribute("instructors", instructorRepository.findAll());
        return "instructor/list";
    }

    @GetMapping("/new")
    public String newInstructorForm(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "instructor/form";
    }

    @GetMapping("/edit/{id}")
    public String editInstructorForm(@PathVariable Integer id, Model model) {
        model.addAttribute("instructor", instructorRepository.findById(id).orElse(new Instructor()));
        return "instructor/form";
    }

    @PostMapping("/save")
    public String saveInstructor(@ModelAttribute("instructor") Instructor instructor) {
        instructorRepository.save(instructor);
        return "redirect:/instructors";
    }

    @GetMapping("/delete/{id}")
    public String deleteInstructor(@PathVariable Integer id) {
        instructorRepository.deleteById(id);
        return "redirect:/instructors";
    }
}
