package fi.tite.akl.repository;

import fi.tite.akl.domain.GameServer;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the GameServer entity.
 */
public interface GameServerRepository extends JpaRepository<GameServer, Long> {

}
