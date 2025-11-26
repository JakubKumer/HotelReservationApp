package com.hotel.ReservationApp.Routes.Dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientDashboardController {

    @GetMapping("/client/dashboard")
    public String clientDashboard() {
        return "client-dashboard";
    }
}
