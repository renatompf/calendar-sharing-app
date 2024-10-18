package io.renatofreire.core.controller;

import io.renatofreire.core.dto.reponses.CalendarDto;
import io.renatofreire.core.service.SharedCalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("shared-calendars")
public class SharedCalendarsController {

    private final SharedCalendarService sharedCalendarService;

    public SharedCalendarsController(SharedCalendarService sharedCalendarService) {
        this.sharedCalendarService = sharedCalendarService;
    }

    @GetMapping
    public ResponseEntity<List<CalendarDto>> getSharedCalendars(@AuthenticationPrincipal final UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(sharedCalendarService.getAllSharedCalendars(userDetails));
    }

}
