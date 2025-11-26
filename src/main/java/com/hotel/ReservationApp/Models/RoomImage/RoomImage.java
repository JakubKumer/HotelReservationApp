package com.hotel.ReservationApp.Models.RoomImage;

import com.hotel.ReservationApp.Models.Room.Room;
import jakarta.persistence.*;

@Entity
@Table(name = "room_images")
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private String imageUrl;

    private boolean mainImage;
}
