package com.gamedepot.admin.controller;

import com.gamedepot.admin.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        Map<String, Object> stats = dashboardService.obtenerEstadisticas();
        model.addAllAttributes(stats);
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }
}
