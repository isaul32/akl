package fi.tite.akl.repository;

import fi.tite.akl.domain.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sauli on 12.6.2016.
 */
@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

}
