package io.renatofreire.core.service;

import io.renatofreire.core.dto.reponses.CalendarDto;
import io.renatofreire.core.dto.requests.CreateCalendarDto;
import io.renatofreire.core.mapper.CalendarMapper;
import io.renatofreire.core.model.Calendar;
import io.renatofreire.core.model.User;
import io.renatofreire.core.repository.CalendarRepository;
import io.renatofreire.core.repository.SharedCalendarsRepository;
import io.renatofreire.core.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final SharedCalendarsRepository sharedCalendarsRepository;
    private final CalendarMapper calendarMapper;

    public CalendarService(UserRepository userRepository, CalendarRepository calendarRepository, SharedCalendarsRepository sharedCalendarsRepository,
                           CalendarMapper calendarMapper) {
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
        this.sharedCalendarsRepository = sharedCalendarsRepository;
        this.calendarMapper = calendarMapper;
    }

    public List<CalendarDto> getAllCalendarsForUser(final UserDetails userDetails){

        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        return calendarRepository.getCalendarsByOwner(userMakingRequest)
                .stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
    }

    public CalendarDto getCalendarById(final UserDetails userDetails, final UUID calendarId){
        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));


        // If the calendar is found, return it otherwise, check if exists a sharedCalendar and return that
        return calendarMapper.toDto(calendarRepository.findById(calendarId)
                .orElseGet(() -> sharedCalendarsRepository.findById_Calendar_Id(calendarId)
                        .filter(sharedCalendar -> sharedCalendarsRepository.existsByCalendarIdAndUserId(calendarId, userMakingRequest.getId()))
                        .map(sharedCalendar -> sharedCalendar.getId().getCalendar())
                        .orElseThrow(() -> new EntityNotFoundException("Calendar was not found"))));
    }

    public CalendarDto createCalendar(final UserDetails userDetails, final CreateCalendarDto createCalendarDto){
        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        if(calendarRepository.existsByNameAndOwner(createCalendarDto.name(), userMakingRequest)){
            throw new EntityExistsException("Calendar with that name already exists");
        }

        return calendarMapper.toDto(calendarRepository.saveAndFlush(new Calendar(createCalendarDto.name(), userMakingRequest)));
    }

    public boolean deleteCalendar(final UserDetails userDetails, final UUID calendarId){
        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        if(!calendarRepository.existsByIdAndOwner(calendarId, userMakingRequest)){
            throw new EntityNotFoundException("Calendar with that id does not exist");
        }

        calendarRepository.deleteById(calendarId);
        return true;
    }


}
