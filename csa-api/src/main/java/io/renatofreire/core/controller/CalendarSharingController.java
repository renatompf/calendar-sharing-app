package io.renatofreire.core.controller;

import io.renatofreire.core.dto.requests.CalendarSharingDeletionDto;
import io.renatofreire.core.dto.requests.CalendarSharingRequestDto;
import io.renatofreire.core.service.CalendarSharingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("calendar-sharing")
public class CalendarSharingController {

    private final CalendarSharingService calendarSharingService;

    public CalendarSharingController(CalendarSharingService calendarSharingService) {
        this.calendarSharingService = calendarSharingService;
    }

    @PostMapping
    public ResponseEntity<?> shareCalendar(@AuthenticationPrincipal final UserDetails userDetails, @RequestBody @Valid CalendarSharingRequestDto calendarSharingRequestDto) {
        calendarSharingService.shareCalendar(userDetails, calendarSharingRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/token-validation")
    public ResponseEntity<?> shareCalendar(@RequestParam(name = "token", required = true) final String token) {
        calendarSharingService.sharingCalendarValidation(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCalendarSharing(@AuthenticationPrincipal final UserDetails userDetails, @NotNull @Valid CalendarSharingDeletionDto calendarSharingDeletionDto) {
        calendarSharingService.deleteCalendarSharing(userDetails, calendarSharingDeletionDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
