package services;

import dtos.BookingCreateDto;
import dtos.BookingUserDto;
import models.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import repositories.BookingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    public @ResponseBody List<BookingUserDto> getAll() {
        List<Booking> bookings = (List<Booking>) ResponseEntity.ok(bookingRepository.findAll());
        return   bookings.stream().map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<BookingUserDto> getByUserId(@PathVariable Long id) { // user calls their own
        List<Booking> bookings = (List<Booking>) ResponseEntity.ok(bookingRepository.findByUserId(id));
        return   bookings.stream().map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<BookingUserDto> getByRestaurant(@PathVariable String name) { // user calls their won
        List<Booking> bookings = (List<Booking>) ResponseEntity.ok(bookingRepository.findByRestaurantName(name));
        return   bookings.stream().map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }


    private BookingUserDto convertToBookingDTO(Booking booking) {
        BookingUserDto dto = new BookingUserDto( //dto without setters
                booking.getId(),
                booking.getUserId(),
                booking.getRestaurantName(),
                booking.getTableNum(),
                booking.getPartySize(),
                booking.getDateMade(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getUpdatedAt()
        );
        // Map only the fields you need for the admin profile
        return dto;
    }
    private BookingCreateDto convertToBookingCreateDTO(Booking booking) {
        BookingCreateDto dto = new BookingCreateDto( //dto with setters
                booking.getId(),
                booking.getUserId(),
                booking.getRestaurantName(),
                booking.getTableNum(),
                booking.getPartySize(),
                booking.getDateMade(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getUpdatedAt()
        );
        // Map only the fields you need for the admin profile
        return dto;
    }
}
