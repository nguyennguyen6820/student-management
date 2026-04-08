package com.studentmanagement.student_management.controller;

import com.studentmanagement.student_management.entity.Subject;
import com.studentmanagement.student_management.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectRepository.findAll());
        return "subject/list";
    }

    @GetMapping("/new")
    public String newSubjectForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subject/form";
    }

    @GetMapping("/edit/{id}")
    public String editSubjectForm(@PathVariable Integer id, Model model) {
        model.addAttribute("subject", subjectRepository.findById(id).orElse(new Subject()));
        return "subject/form";
    }

    @PostMapping("/save")
    public String saveSubject(@ModelAttribute("subject") Subject subject) {
        subjectRepository.save(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Integer id) {
        subjectRepository.deleteById(id);
        return "redirect:/subjects";
    }
}
