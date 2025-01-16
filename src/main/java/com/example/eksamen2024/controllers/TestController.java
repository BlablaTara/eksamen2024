package com.example.eksamen2024.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://localhost:5173"})
@RestController
public class TestController {


    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/testing")
    public String testing() {
        String response = "Hello Testing";
        return response;
    }


}
