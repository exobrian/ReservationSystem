package com.amenity_reservation_system.service;

import com.amenity_reservation_system.model.Reservation;
import com.amenity_reservation_system.model.User;
import com.amenity_reservation_system.repos.ReservationRepository;
import com.amenity_reservation_system.repos.UserRepository;
import com.amenity_reservation_system.repos.CapacityRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CapacityRepository capacityRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final UserRepository userRepository,
                              final CapacityRepository capacityRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.capacityRepository = capacityRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation get(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final Reservation reservation) {
        int capacity = capacityRepository.findByAmenityType(reservation.getAmenityType()).getCapacity();
        int overlappingReservations = reservationRepository.findReservationsByReservationDateAndStartTimeBeforeAndEndTimeAfterOrStartTimeBetween(
                reservation.getReservationDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStartTime(),
                reservation.getEndTime()
        ).size();

        System.out.println("Current count for " + reservation.getAmenityType() + " is " + overlappingReservations);
        if (overlappingReservations > capacity){
            throw new IllegalArgumentException("Current reservations for " + reservation.getAmenityType() + " is full.");
        }
        return reservationRepository.save(reservation).getId();
    }

    public void update(final Long id, final Reservation reservation) {
        final Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        reservationRepository.save(reservation);
    }

    public void delete(final Long id) {
        reservationRepository.deleteById(id);
    }
}