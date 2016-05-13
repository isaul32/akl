package com.pyrenty.akl.repository;

import com.pyrenty.akl.domain.Authority;
import com.pyrenty.akl.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
