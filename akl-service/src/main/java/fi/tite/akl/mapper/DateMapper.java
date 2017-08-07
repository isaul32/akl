package fi.tite.akl.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring")
public class DateMapper {
    public Long dateTimeToAge(LocalDate localDate) {
        if (localDate == null) {
            return 0L;
        }

        return (long) Period.between(localDate, LocalDate.now()).getYears();
    }
}
