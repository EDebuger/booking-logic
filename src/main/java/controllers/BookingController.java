package controllers;

import dtos.BookingUserDto;
import dtos.CreateBookingDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import services.BookingService;

import java.util.List;

@RestController()
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/getAll")
    public @ResponseBody ResponseEntity<List<BookingUserDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @PreAuthorize("hasAnyRole()")
    @GetMapping("/getByUserId/{id}") // every booking from user
    public @ResponseBody ResponseEntity<List<BookingUserDto>> getByUserId(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(bookingService.getByUserId(id));
    }

    @PreAuthorize("hasAnyRole()")
    @GetMapping("/getByRestaurant/{name}") // every booking from user
    public @ResponseBody ResponseEntity<List<BookingUserDto>> getByRestaurant(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok(bookingService.getByRestaurant(name));
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/



    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/deleteBooking{id}") // delete booking for user
    public @ResponseBody ResponseEntity<String> deleteBooking(@PathVariable(value = "id") Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.status(HttpStatus.OK).body("Booking deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/postBooking") // create booking for user
    public @ResponseBody ResponseEntity<BookingUserDto> createBooking(@Valid @RequestBody CreateBookingDto createBookingDto) {
        BookingUserDto created = bookingService.createBooking(createBookingDto);
        return ResponseEntity.status(201).body(created); // "resource created"
    }

    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/
}
