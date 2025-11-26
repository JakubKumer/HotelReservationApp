package com.hotel.ReservationApp.API.User;

import com.hotel.ReservationApp.Models.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
