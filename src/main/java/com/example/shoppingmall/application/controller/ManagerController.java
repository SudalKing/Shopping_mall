package com.example.shoppingmall.application.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @GetMapping("/manager-page")
    public String managerPage(){
        return "managerPage";
    }
}
