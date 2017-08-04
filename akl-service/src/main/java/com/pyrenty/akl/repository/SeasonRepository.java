package com.pyrenty.akl.repository;

import com.pyrenty.akl.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    Season findByArchived(boolean isArchived);
}
