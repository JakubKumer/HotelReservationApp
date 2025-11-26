package com.hotel.ReservationApp.Controllers.User;

import com.hotel.ReservationApp.API.User.LoginRequest;
import com.hotel.ReservationApp.API.User.LoginResponse;
import com.hotel.ReservationApp.Models.User.User;
import com.hotel.ReservationApp.Repositories.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class LoginUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginUserController(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Nieprawidłowy email lub hasło"
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nieprawidłowy email lub hasło"
            );
        }

        return new LoginResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }
}

