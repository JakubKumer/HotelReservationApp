package com.hotel.ReservationApp.API.Room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRoomRequest {

    @NotBlank(message = "Podaj numer pokoju")
    private String roomNumber;

    @NotNull(message = "Podaj numer piętra")
    private Integer floor;

    @NotNull(message = "Podaj pojemność pokoju")
    @Min(value = 1, message = "Pojemność pokoju musi być co najmniej 1")
    private Integer capacity;

    @NotNull(message = "Podaj cenę za noc")
    @Positive(message = "Cena za noc musi być dodatnia")
    private BigDecimal pricePerNight;

    private String description;
}
