package fi.tite.akl.repository;


import fi.tite.akl.domain.statistics.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsEventRepository extends JpaRepository<Event, Long> {

}
