package com.hotel.ReservationApp.Routes.RoomDetails;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomDetails {

    @GetMapping("/client/room/{id}")
    public String clientRoomPage() {
        return "client-room-details"; // nazwa pliku HTML
    }
}
