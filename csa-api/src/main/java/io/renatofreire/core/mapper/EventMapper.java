package io.renatofreire.core.mapper;

import io.renatofreire.core.dto.reponses.EventDto;
import io.renatofreire.core.model.Event;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    @Mapping(source = "calendarName", target = "calendar.name")
    Event toEntity(EventDto eventDto);

    @Mapping(source = "calendar.name", target = "calendarName")
    EventDto toDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "calendarName", target = "calendar.name")
    Event partialUpdate(EventDto eventDto, @MappingTarget Event event);
}