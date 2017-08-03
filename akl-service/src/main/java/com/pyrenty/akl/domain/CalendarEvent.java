package com.pyrenty.akl.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Sauli on 12.6.2016.
 */
@Entity
@Table(name = "akl_calendar_event")
public class CalendarEvent {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    private LocalDateTime start;

    @Getter
    @Setter
    private LocalDateTime end;
}
