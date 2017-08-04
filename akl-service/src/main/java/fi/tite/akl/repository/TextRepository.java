package fi.tite.akl.repository;

import fi.tite.akl.domain.Text;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Text entity.
 */
public interface TextRepository extends JpaRepository<Text, Long> {

}
