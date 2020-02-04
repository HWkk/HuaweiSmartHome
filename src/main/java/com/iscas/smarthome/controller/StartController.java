package com.iscas.smarthome.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class StartController {

    @RequestMapping("/start")
    public String start() {
        System.out.println("start");
        return "Hello, World!";
    }

    @RequestMapping("/get_attributes")
    public String getAttributes(HttpServletRequest request) {
        System.out.println(request.getParameter("entity_name"));
        return "Hello, World!";
    }
}
