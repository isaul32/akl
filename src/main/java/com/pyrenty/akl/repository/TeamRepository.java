package com.pyrenty.akl.repository;

import com.pyrenty.akl.domain.Team;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Team entity.
 */
public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query("SELECT t FROM Team t " +
        "LEFT JOIN FETCH t.members " +
        "LEFT JOIN FETCH t.standins " +
        "WHERE t.id = :id")
    Team findOne(@Param("id") Long id);

}
