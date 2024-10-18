package io.renatofreire.core.service;

import io.renatofreire.core.dto.requests.CalendarSharingDeletionDto;
import io.renatofreire.core.dto.requests.CalendarSharingRequestDto;
import io.renatofreire.core.dto.requests.ShareCalendarMessageDto;
import io.renatofreire.core.enums.CalendarSharingMessageType;
import io.renatofreire.core.exceptions.EmailAlreadyValidatedException;
import io.renatofreire.core.exceptions.TokenAlreadyExpiredException;
import io.renatofreire.core.model.*;
import io.renatofreire.core.repository.CalendarRepository;
import io.renatofreire.core.repository.SharedCalendarValidationTokenRepository;
import io.renatofreire.core.repository.SharedCalendarsRepository;
import io.renatofreire.core.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CalendarSharingService {

    private final CalendarRepository calendarRepository;
    private final SharedCalendarsRepository sharedCalendarsRepository;
    private final UserRepository userRepository;
    private final SharedCalendarValidationTokenRepository sharedCalendarValidationTokenRepository;
    private final RabbitService rabbitService;

    public CalendarSharingService(CalendarRepository calendarRepository, SharedCalendarsRepository sharedCalendarsRepository, UserRepository userRepository, SharedCalendarValidationTokenRepository sharedCalendarValidationTokenRepository, RabbitService rabbitService) {
        this.calendarRepository = calendarRepository;
        this.sharedCalendarsRepository = sharedCalendarsRepository;
        this.userRepository = userRepository;
        this.sharedCalendarValidationTokenRepository = sharedCalendarValidationTokenRepository;
        this.rabbitService = rabbitService;
    }

    public boolean shareCalendar(final UserDetails userDetails, final CalendarSharingRequestDto calendarSharingRequestDto) {

        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        final Calendar calendar = calendarRepository.findById(calendarSharingRequestDto.calendarId())
                .orElseThrow(() -> new EntityNotFoundException("Calendar was not found"));

        if(!calendar.getOwner().equals(userMakingRequest)) {
            throw new EntityNotFoundException("Calendar was not found");
        }

        final User sharedWith = userRepository.findByEmail(calendarSharingRequestDto.sharedWith())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        if(sharedCalendarsRepository.existsByCalendarIdAndUserId(calendar.getId(), sharedWith.getId())){
            throw new EntityExistsException("Calendar already shared with user");
        }

        SharedCalendarValidationToken sharedCalendarValidationToken = new SharedCalendarValidationToken(
                UUID.randomUUID().toString().replace("-", ""),
                LocalDateTime.now(),
                null,
                calendarSharingRequestDto.calendarPermissions(),
                calendar,
                sharedWith
        );

        sharedCalendarValidationToken = sharedCalendarValidationTokenRepository.save(sharedCalendarValidationToken);

        rabbitService.sendMessage(new ShareCalendarMessageDto(
                calendar.getName(),
                userMakingRequest.getFirstName() + " " + userMakingRequest.getLastName(),
                sharedWith.getEmail(),
                sharedWith.getFirstName() + " " + userMakingRequest.getLastName(),
                sharedCalendarValidationToken.getToken(),
                CalendarSharingMessageType.CREATION
        ));

        return true;
    }

    public boolean sharingCalendarValidation(final String token){
        SharedCalendarValidationToken sharedCalendarValidationToken = sharedCalendarValidationTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Verification token not found"));

        if (sharedCalendarValidationToken.getConfirmedAt() != null) {
            throw new EntityExistsException("Token already confirmed");
        }

        if (LocalDateTime.now().isAfter(sharedCalendarValidationToken.getCreatedAt().plusMinutes(30))) {
            throw new TokenAlreadyExpiredException("token expired");
        }

        sharedCalendarValidationToken.setConfirmedAt(LocalDateTime.now());
        sharedCalendarValidationTokenRepository.saveAndFlush(sharedCalendarValidationToken);

        SharedCalendars sharedCalendars = new SharedCalendars(new SharedCalendarPK(
                sharedCalendarValidationToken.getCalendar(), sharedCalendarValidationToken.getSharedWith()),
                sharedCalendarValidationToken.getCalendarPermissions());

        sharedCalendarsRepository.saveAndFlush(sharedCalendars);
        return true;
    }

    public boolean deleteCalendarSharing(final UserDetails userDetails, final CalendarSharingDeletionDto calendarSharingDeletionDto) {

        final User userMakingRequest = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        final Calendar calendar = calendarRepository.findById(calendarSharingDeletionDto.calendarId())
                .orElseThrow(() -> new EntityNotFoundException("Calendar was not found"));

        if(!calendar.getOwner().equals(userMakingRequest)) {
            throw new EntityNotFoundException("Calendar was not found");
        }

        final User sharedWith = userRepository.findByEmail(calendarSharingDeletionDto.email())
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        if(sharedCalendarsRepository.existsByCalendarIdAndUserId(calendar.getId(), sharedWith.getId())){
            throw new EntityExistsException("Calendar not shared with user");
        }

        SharedCalendars sharedCalendars = sharedCalendarsRepository.findByCalendarIdAndUserId(calendar.getId(), sharedWith.getId())
                .orElseThrow(() -> new EntityNotFoundException("Sharing was not found"));

        sharedCalendarsRepository.delete(sharedCalendars);

        rabbitService.sendMessage(new ShareCalendarMessageDto(
                calendar.getName(),
                userMakingRequest.getFirstName() + " " + userMakingRequest.getLastName(),
                sharedWith.getEmail(),
                sharedWith.getFirstName() + " " + userMakingRequest.getLastName(),
                null,
                CalendarSharingMessageType.DELETION
        ));

        return true;
    }

}
