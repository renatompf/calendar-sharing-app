package io.renatofreire.core.controller;

import io.renatofreire.core.dto.reponses.CalendarDto;
import io.renatofreire.core.dto.requests.CreateCalendarDto;
import io.renatofreire.core.service.CalendarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public ResponseEntity<List<CalendarDto>> getAllCalendars(@AuthenticationPrincipal final UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(calendarService.getAllCalendarsForUser(userDetails));
    }

    @GetMapping("/{calendarId}")
    public ResponseEntity<CalendarDto> getCalendarById(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable(name = "calendarId") UUID calendarId) {
        return ResponseEntity.status(HttpStatus.OK).body(calendarService.getCalendarById(userDetails, calendarId));
    }

    @PostMapping
    public ResponseEntity<CalendarDto> createCalendar(@AuthenticationPrincipal final UserDetails userDetails, @Valid @RequestBody final CreateCalendarDto createCalendarDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarService.createCalendar(userDetails, createCalendarDto));
    }

    @DeleteMapping("/{calendarId}")
    public ResponseEntity<?> deleteCalendar(@AuthenticationPrincipal final UserDetails userDetails, @PathVariable(name = "calendarId") final UUID calendarId){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(calendarService.deleteCalendar(userDetails, calendarId));
    }


}
