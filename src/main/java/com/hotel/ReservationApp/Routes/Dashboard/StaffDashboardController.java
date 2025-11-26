package com.hotel.ReservationApp.Routes.Dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffDashboardController {

    @GetMapping("/staff/dashboard")
    public String staffDashboard() {
        return "staff-dashboard";
    }
}
