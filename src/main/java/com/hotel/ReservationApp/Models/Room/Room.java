package com.hotel.ReservationApp.Models.Room;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    private String description;

    @Column(nullable = false)
    private boolean active;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(name = "image_content_type")
    private String imageContentType;
}
