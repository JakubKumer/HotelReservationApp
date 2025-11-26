package com.hotel.ReservationApp.Controllers.Room;

import com.hotel.ReservationApp.API.Room.CreateRoomRequest;
import com.hotel.ReservationApp.Models.Room.Room;
import com.hotel.ReservationApp.Services.Room.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room createRoom(@Valid @RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @PatchMapping("/{id}/deactivate")
    public Room deactivateRoom(@PathVariable Long id) {
        return roomService.deactivateRoom(id);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadRoomImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        roomService.uploadRoomImage(id, file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getRoomImage(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);

        byte[] data = room.getImageData();
        if (data == null || data.length == 0) {
            throw new ResponseStatusException(
                    NOT_FOUND,
                    "Pokój nie ma przypisanego zdjęcia"
            );
        }

        String contentType = room.getImageContentType();
        MediaType mediaType;
        try {
            mediaType = (contentType != null)
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.IMAGE_JPEG;
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(data);
    }
}
