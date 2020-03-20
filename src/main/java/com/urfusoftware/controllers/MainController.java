package com.urfusoftware.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String greeting()
    {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("some", "Hello again, this is our main page!");
        return "main";
    }



}