package controllers;

import dtos.BookingUserDto;
import dtos.CreateBookingDto;
import jakarta.validation.Valid;
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

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

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

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/



    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PostMapping("/deleteBooking{id}") // create booking for user
    public @ResponseBody ResponseEntity<BookingUserDto> deleteBooking(@PathVariable(value = "id") Long id) {

        return  // "resource created"
    }

    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PostMapping("/postBooking") // create booking for user
    public @ResponseBody ResponseEntity<BookingUserDto> createBooking(@Valid @RequestBody CreateBookingDto createBookingDto) {
        BookingUserDto created = bookingService.createBooking(createBookingDto);
        return ResponseEntity.status(201).body(created); // "resource created"
    }

    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/
}
