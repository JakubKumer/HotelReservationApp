package com.hotel.ReservationApp.Repositories.Role;

import com.hotel.ReservationApp.Models.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
