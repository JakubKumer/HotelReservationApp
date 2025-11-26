package com.hotel.ReservationApp.Services.Room;

import com.hotel.ReservationApp.API.Room.CreateRoomRequest;
import com.hotel.ReservationApp.Models.Room.Room;
import com.hotel.ReservationApp.Repositories.Room.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nie znaleziono pokoju o ID: " + id
                ));
    }

    public Room createRoom(CreateRoomRequest request) {

        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Pokój o numerze " + request.getRoomNumber() + " już istnieje"
            );
        }

        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .capacity(request.getCapacity())
                .pricePerNight(request.getPricePerNight())
                .description(request.getDescription())
                .active(true)
                .build();

        return roomRepository.save(room);
    }

    public Room deactivateRoom(Long id) {
        Room room = getRoomById(id);
        room.setActive(false);
        return roomRepository.save(room);
    }

    public void uploadRoomImage(Long roomId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Plik ze zdjęciem jest pusty"
            );
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Dozwolone są tylko pliki graficzne (image/*)"
            );
        }

        Room room = getRoomById(roomId);

        try {
            room.setImageData(file.getBytes());
            room.setImageContentType(contentType);
            roomRepository.save(room);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    INTERNAL_SERVER_ERROR,
                    "Nie udało się odczytać pliku zdjęcia",
                    e
            );
        }
    }

}