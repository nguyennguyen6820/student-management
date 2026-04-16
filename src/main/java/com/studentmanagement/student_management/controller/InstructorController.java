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

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping("/save")
    public String saveInstructor(@ModelAttribute("instructor") Instructor instructor) {
        try {
            if (instructor.getId() == null && instructor.getUser() == null) {
                com.studentmanagement.student_management.entity.User user = new com.studentmanagement.student_management.entity.User();
                String baseUsername = (instructor.getEmail() != null && instructor.getEmail().contains("@")) ? instructor.getEmail().split("@")[0] : "ins";
                String username = baseUsername + "_" + (System.currentTimeMillis() % 10000);
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("123456"));
                user.setFullName(instructor.getFullName() != null ? instructor.getFullName() : "Giang Vien");
                user.setRole(com.studentmanagement.student_management.entity.Role.ROLE_INSTRUCTOR);
                instructor.setUser(user);
            }
            instructorRepository.save(instructor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR WHILE SAVING INSTRUCTOR: " + e.getMessage(), e);
        }
        return "redirect:/instructors";
    }

    @GetMapping("/delete/{id}")
    public String deleteInstructor(@PathVariable Integer id) {
        instructorRepository.deleteById(id);
        return "redirect:/instructors";
    }
}
