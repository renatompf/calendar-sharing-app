package io.renatofreire.core.repository;

import io.renatofreire.core.enums.CalendarPermissions;
import io.renatofreire.core.model.SharedCalendarPK;
import io.renatofreire.core.model.SharedCalendars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SharedCalendarsRepository extends JpaRepository<SharedCalendars, SharedCalendarPK> {

    @Query("select (count(s) > 0) from SharedCalendars s where s.id.calendar.id = :calendarId and s.id.user.id = :userId")
    boolean existsByCalendarIdAndUserId(final UUID calendarId, final UUID userId);

    @Query("select s from SharedCalendars s LEFT JOIN Calendar c on s.id.calendar.id = c.id where s.id.calendar.id = :id")
    Optional<SharedCalendars> findById_Calendar_Id(UUID id);

    @Query("select s from SharedCalendars s where s.id.calendar.id = :calendarId and s.id.user.id = :userId")
    Optional<SharedCalendars> findByCalendarIdAndUserId(final UUID calendarId, final UUID userId);

    @Query("select (count(s) > 0) from SharedCalendars s where s.id.calendar.id = :calendarId and s.id.user.id = :id and s.permission = :calendarPermissions")
    boolean existsByCalendarIdAndUserIdAndPermission(UUID calendarId, UUID id, CalendarPermissions calendarPermissions);

    @Query("select s from SharedCalendars s WHERE s.id.user.id = :userId")
    List<SharedCalendars> findAllSharedCalendarsByUserId(UUID userId);

}