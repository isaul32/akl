package fi.tite.akl.repository;

import fi.tite.akl.domain.MatchRequest;
import fi.tite.akl.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

    List<MatchRequest> findByTeam1OrTeam2(Team team1, Team team2);
}
