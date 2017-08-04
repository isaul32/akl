package fi.tite.akl.repository;

import fi.tite.akl.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    Season findByArchived(boolean isArchived);
}
