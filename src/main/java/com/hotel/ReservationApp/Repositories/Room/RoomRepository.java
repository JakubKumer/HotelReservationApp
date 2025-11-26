package com.hotel.ReservationApp.Repositories.Room;

import com.hotel.ReservationApp.Models.Room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomNumber(String roomNumber);
}
