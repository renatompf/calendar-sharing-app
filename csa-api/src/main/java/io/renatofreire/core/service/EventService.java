package io.renatofreire.core.service;

import io.renatofreire.core.dto.reponses.EventDto;
import io.renatofreire.core.dto.requests.CreateEventDto;
import io.renatofreire.core.enums.CalendarPermissions;
import io.renatofreire.core.mapper.EventMapper;
import io.renatofreire.core.model.Calendar;
import io.renatofreire.core.model.Event;
import io.renatofreire.core.model.User;
import io.renatofreire.core.repository.CalendarRepository;
import io.renatofreire.core.repository.EventsRepository;
import io.renatofreire.core.repository.SharedCalendarsRepository;
import io.renatofreire.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final SharedCalendarsRepository sharedCalendarsRepository;
    private final EventMapper eventMapper;

    public EventService(EventsRepository eventsRepository, UserRepository userRepository, CalendarRepository calendarRepository, SharedCalendarsRepository sharedCalendarsRepository,
                        EventMapper eventMapper) {
        this.eventsRepository = eventsRepository;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
        this.sharedCalendarsRepository = sharedCalendarsRepository;
        this.eventMapper = eventMapper;
    }


    public List<EventDto> getAllEventsForCalendar(final UserDetails userDetails, final UUID calendarId, final LocalDateTime startDate, final LocalDateTime endDate) {

        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        checkUserAccessToCalendar(calendarId, userMakingRequest);

        if(startDate != null) {

            if(endDate == null){
                return eventsRepository.findByCalendar_IdAndStartTimeGreaterThanEqual(calendarId, startDate)
                        .stream()
                        .map(eventMapper::toDto)
                        .collect(Collectors.toList());
            }

            return eventsRepository.getEventsByCalendarBetweenDates(calendarId, startDate, endDate)
                    .stream()
                    .map(eventMapper::toDto)
                    .collect(Collectors.toList());
        }

        return eventsRepository.getEventByCalendar_Id(calendarId)
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventDto getEvent(UserDetails userDetails, final UUID eventId) {
        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        Event event = eventsRepository.getEventWithCalendar(eventId).orElseThrow(() -> new EntityNotFoundException("Event was not found"));

        checkUserAccessToCalendar(event.getCalendar().getId(), userMakingRequest);

        return eventMapper.toDto(event);
    }

    public EventDto createEvent(UserDetails userDetails, final CreateEventDto createEventDto){
        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        Calendar calendar = calendarRepository.findById(createEventDto.calendarId()).orElseThrow(() -> new EntityNotFoundException("Calendar was not found"));

        if(!hasWriteAccessToCalendar(calendar.getId(), userMakingRequest)){
            throw new EntityNotFoundException("Calendar was not found");
        }

        if(createEventDto.startTime().isAfter(createEventDto.endTime())){
            throw new IllegalStateException("Start time cannot be after end time");
        }

        return eventMapper.toDto(eventsRepository.saveAndFlush(
                new Event(
                        createEventDto.title(),
                        createEventDto.description(),
                        createEventDto.startTime(),
                        createEventDto.endTime(),
                        calendar
                )
        ));
    }

    public boolean deleteEvent(UserDetails userDetails, final UUID eventId) {
        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        Event event = eventsRepository.getEventWithCalendar(eventId).orElseThrow(() -> new EntityNotFoundException("Event was not found"));

        Calendar calendar = calendarRepository.findById(event.getCalendar().getId()).orElseThrow(() -> new EntityNotFoundException("Calendar was not found"));

        if(!hasWriteAccessToCalendar(calendar.getId(), userMakingRequest)){
            throw new EntityNotFoundException("Calendar was not found");
        }

        eventsRepository.deleteById(eventId);
        return true;
    }

    private boolean hasWriteAccessToCalendar(UUID calendarId, User user) {
        // Check if the user is the owner or has READ_WRITE permissions
        return calendarRepository.existsByIdAndOwner(calendarId, user) ||
                sharedCalendarsRepository.existsByCalendarIdAndUserIdAndPermission(calendarId, user.getId(), CalendarPermissions.READ_WRITE);
    }

    private void checkUserAccessToCalendar(UUID calendarId, User userMakingRequest) {
        if(!calendarRepository.existsByIdAndOwner(calendarId, userMakingRequest) &&
                !sharedCalendarsRepository.existsByCalendarIdAndUserId(calendarId, userMakingRequest.getId())){
            throw new EntityNotFoundException("Calendar was not found");
        }
    }

}
