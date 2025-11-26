package com.hotel.ReservationApp.Services.User;

import com.hotel.ReservationApp.API.User.CreateUserRequest;
import com.hotel.ReservationApp.Models.Role.Role;
import com.hotel.ReservationApp.Models.User.User;
import com.hotel.ReservationApp.Repositories.Role.RoleRepository;
import com.hotel.ReservationApp.Repositories.User.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Użytkownik z podanym adresem email już istnieje"
            );
        }
        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Rola klienta (ID 1) nie istnieje w bazie!"
                ));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika: " + id));
    }
}

