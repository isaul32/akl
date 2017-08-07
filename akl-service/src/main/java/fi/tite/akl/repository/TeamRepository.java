package fi.tite.akl.repository;

import fi.tite.akl.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Spring Data JPA repository for the Team entity.
 */
public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findBySeasonIdAndNameContainingIgnoreCase(Long seasonId, String name, Pageable pageable);
    Page<Team> findBySeasonId(Long season, Pageable pageable);
    Page<Team> findByActivated(Boolean activated, Pageable pageable);
    Team findOneByActivated( Long id, Boolean activated);
    Team findOneByMembersIdAndSeasonId(Long userId, Long seasonId);

}
