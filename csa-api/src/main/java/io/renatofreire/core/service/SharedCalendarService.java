package io.renatofreire.core.service;

import io.renatofreire.core.dto.reponses.CalendarDto;
import io.renatofreire.core.mapper.CalendarMapper;
import io.renatofreire.core.model.User;
import io.renatofreire.core.repository.SharedCalendarsRepository;
import io.renatofreire.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SharedCalendarService {

    private final SharedCalendarsRepository sharedCalendarsRepository;
    private final UserRepository userRepository;
    private final CalendarMapper calendarMapper;

    public SharedCalendarService(SharedCalendarsRepository sharedCalendarsRepository, UserRepository userRepository,
                                 CalendarMapper calendarMapper) {
        this.sharedCalendarsRepository = sharedCalendarsRepository;
        this.userRepository = userRepository;
        this.calendarMapper = calendarMapper;
    }

    public List<CalendarDto> getAllSharedCalendars(final UserDetails userDetails) {

        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        return sharedCalendarsRepository.findAllSharedCalendarsByUserId(userMakingRequest.getId())
                .stream()
                .map(sharedCalendars -> sharedCalendars.getId().getCalendar())
                .toList()
                .stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
    }
}
