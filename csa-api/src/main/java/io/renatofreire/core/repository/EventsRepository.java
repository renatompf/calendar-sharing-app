package io.renatofreire.core.repository;

import io.renatofreire.core.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventsRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e from Event e LEFT JOIN Calendar c on c.id = e.calendar.id where e.calendar.id = :calendarId and e.startTime BETWEEN :startTime AND :endTime")
    List<Event> getEventsByCalendarBetweenDates(UUID calendarId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select e from Event e LEFT JOIN Calendar c on c.id = e.calendar.id where e.calendar.id = :calendarId and e.startTime >= :startTime")
    List<Event> findByCalendar_IdAndStartTimeGreaterThanEqual(UUID calendarId, LocalDateTime startTime);

    List<Event> getEventByCalendar_Id(UUID calendarId);

    @Query("SELECT e from Event e left join Calendar c on e.calendar.id = c.id where e.id = :eventId")
    Optional<Event> getEventWithCalendar(UUID eventId);
}
