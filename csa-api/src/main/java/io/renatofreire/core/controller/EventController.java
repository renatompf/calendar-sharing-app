package io.renatofreire.core.controller;

import io.renatofreire.core.dto.reponses.EventDto;
import io.renatofreire.core.dto.requests.CreateEventDto;
import io.renatofreire.core.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/calendar/{calendarId}")
    public ResponseEntity<List<EventDto>> getAllEvents(@AuthenticationPrincipal final UserDetails userDetails,
                                                       @PathVariable("calendarId") final UUID calendarId,
                                                       @RequestParam(value = "startDate", required = false) final LocalDateTime startDate,
                                                       @RequestParam(value = "endDate", required = false) final LocalDateTime endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventsForCalendar(userDetails, calendarId, startDate, endDate));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEvent(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable("eventId") final UUID eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEvent(userDetails, eventId));
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@AuthenticationPrincipal final UserDetails userDetails, @RequestBody final CreateEventDto event) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(userDetails, event));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable("eventId") final UUID eventId){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.deleteEvent(userDetails, eventId));
    }



}
