package com.pyrenty.akl.repository;


import com.pyrenty.akl.domain.statistics.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsEventRepository extends JpaRepository<Event, Long> {

}
