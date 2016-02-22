package com.pyrenty.akl.repository;

import com.pyrenty.akl.domain.LocalizedText;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the LocalizedText entity.
 */
public interface LocalizedTextRepository extends JpaRepository<LocalizedText,Long> {

}
