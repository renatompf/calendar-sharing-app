package io.renatofreire.core.mapper;

import io.renatofreire.core.dto.reponses.SharedCalendarsDto;
import io.renatofreire.core.model.SharedCalendars;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SharedCalendarsMapper {
    @Mapping(source = "idUserEmail", target = "id.user.email")
    @Mapping(source = "idCalendarName", target = "id.calendar.name")
    @Mapping(source = "idCalendarId", target = "id.calendar.id")
    SharedCalendars toEntity(SharedCalendarsDto sharedCalendarsDto);

    @InheritInverseConfiguration(name = "toEntity")SharedCalendarsDto toDto(SharedCalendars sharedCalendars);

    @InheritConfiguration(name = "toEntity")@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)SharedCalendars partialUpdate(SharedCalendarsDto sharedCalendarsDto, @MappingTarget SharedCalendars sharedCalendars);
}