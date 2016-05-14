package com.pyrenty.akl.repository;

import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Team entity.
 */
@SuppressWarnings("JpaQlInspection")
public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query("SELECT t FROM Team t " +
        "LEFT JOIN FETCH t.members " +
        "LEFT JOIN FETCH t.standins " +
        "WHERE t.id = :id")
    Team findOne(@Param("id") Long id);

    Page<Team> findByActivated(Boolean activated, Pageable pageable);

    @Query("SELECT t FROM Team t " +
            "LEFT JOIN FETCH t.members " +
            "LEFT JOIN FETCH t.standins " +
            "WHERE t.id = :id AND t.activated = :activated")
    Team findOneByActivated(@Param("id") Long id,
                            @Param("activated") Boolean activated);

    @Query("Select t from Team t " +
        "LEFT JOIN FETCH t.members m " +
        "LEFT JOIN FETCH t.standins s " +
        "WHERE t.captain.id = :user OR " +
        "m.id = :user OR " +
        "s.id = :user")
    Team findOneForUser(@Param("user") Long userId);
}
