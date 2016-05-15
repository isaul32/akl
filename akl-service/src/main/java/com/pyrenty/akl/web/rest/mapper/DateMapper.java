package com.pyrenty.akl.web.rest.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public class DateMapper {
    public Long dateTimeToAge(DateTime dateTime) {
        if (dateTime == null) {
            return 0L;
        }
        Period period = new Period(dateTime.toLocalDate(), new LocalDate(), PeriodType.yearMonthDay());
        return (long) period.getYears();
    }
}
