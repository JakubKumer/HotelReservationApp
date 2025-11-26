package com.hotel.ReservationApp.API.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Podaj email")
    @Email(message = "Podaj poprawny adres email")
    private String email;

    @NotBlank(message = "Podaj hasło")
    private String password;
}
