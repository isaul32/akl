package com.pyrenty.akl.repository;

import com.pyrenty.akl.domain.Team;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
public interface TeamRepository extends JpaRepository<Team,Long> {

}
