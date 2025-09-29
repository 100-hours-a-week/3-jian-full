package com.example.spring_practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = true, defaultValue = "world") String name,
            Model model
    ){
        String message = "Hello, " + name + "!";

        model.addAttribute("message", message);
        return "greeting";
    }
}
