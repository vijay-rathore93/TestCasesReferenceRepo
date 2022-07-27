package com.dentalportal.controller;

import com.sun.xml.bind.v2.TODO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    @GetMapping("/login")
    public String getLogin() {
        return "/pages/login";
    }

    @GetMapping("/fail")
    public String getSharedError() {
        return "error";
    }

    @GetMapping("/user/dashboard")
    public String getUserDashboard() {
        return "/pages/user-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String getAdminDashboard() {
        return "/pages/admin-dashboard";
    }
}
