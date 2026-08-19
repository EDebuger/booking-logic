package services;

import dtos.CreateBookingDto;
import dtos.BookingUserDto;
import jakarta.transaction.Transactional;
import models.Booking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import repositories.BookingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    public @ResponseBody List<BookingUserDto> getAll() {
        System.out.println("Fetching all bookings{}");
        List<Booking> bookings = (List<Booking>) ResponseEntity.ok(bookingRepository.findAll());
        return   bookings.stream().map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<BookingUserDto> getByUserId(@PathVariable Long id) { // user calls their own
        System.out.println("Fetching all of bookings{} from user "+ id);
        List<Booking> bookings = (List<Booking>) ResponseEntity.ok(bookingRepository.findByUserId(id));
        return   bookings.stream().map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<BookingUserDto> getByRestaurant(@PathVariable String name) { // user calls their own
        System.out.println("Fetching all bookings{} for restaurant "+ name);
        List<Booking> bookings = (List<Booking>) ResponseEntity.ok(bookingRepository.findByRestaurantName(name));
        return   bookings.stream().map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Transactional // guarantees more than one query can be run in the same session
    public @ResponseBody BookingUserDto createBooking(@PathVariable CreateBookingDto cb) {
        Booking booking = toEntity(cb);
        System.out.println("Inserting new booking for restaurant "+cb.getRestaurantName()+"for date "+ cb.getBookingDate());
        Booking saved;
        try {
        saved = bookingRepository.save(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return convertToBookingDTO(saved);
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

    // Request DTO, as in POST | from DTO -> entity
    public Booking toEntity(CreateBookingDto dto) {
        return new Booking(
                dto.getUserId(),
                dto.getRestaurantName(),
                dto.getTableNum(),
                dto.getPartySize(),
                dto.getBookingDate()
        );
    }
}
