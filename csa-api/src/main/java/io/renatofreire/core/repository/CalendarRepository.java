package io.renatofreire.core.repository;

import io.renatofreire.core.model.Calendar;
import io.renatofreire.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, UUID> {

  List<Calendar> getCalendarsByOwner(User owner);

  boolean existsByNameAndOwner(String name, User owner);

  boolean existsByIdAndOwner(UUID id, User owner);

}