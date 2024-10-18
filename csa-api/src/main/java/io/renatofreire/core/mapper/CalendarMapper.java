package io.renatofreire.core.mapper;

import io.renatofreire.core.dto.reponses.CalendarDto;
import io.renatofreire.core.model.Calendar;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalendarMapper {
    @Mapping(source = "ownerLastName", target = "owner.lastName")
    @Mapping(source = "ownerFirstName", target = "owner.firstName")
    Calendar toEntity(CalendarDto calendarDto);

    @InheritInverseConfiguration(name = "toEntity")CalendarDto toDto(Calendar calendar);

    @InheritConfiguration(name = "toEntity")@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)Calendar partialUpdate(CalendarDto calendarDto, @MappingTarget Calendar calendar);
}