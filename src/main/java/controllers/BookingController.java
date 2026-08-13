package controllers;

import dtos.BookingUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.BookingService;

import java.util.List;

@ControllerAdvice(name = "bookingController")
@RequestMapping("/Bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/getAll")
    public @ResponseBody ResponseEntity<List<BookingUserDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @GetMapping("/getByUserId/{id}") // every booking from user
    public @ResponseBody ResponseEntity<List<BookingUserDto>> getByUserId(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(bookingService.getByUserId(id));
    }

    @GetMapping("/getByRestaurant/{name}") // every booking from user
    public @ResponseBody ResponseEntity<List<BookingUserDto>> getByRestaurant(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok(bookingService.getByRestaurant(name));
    }
}
