package com.pyrenty.akl.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "akl_season")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 16)
    @Column(name = "short_name", length = 16)
    private String shortName;

    @Size(max = 255)
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "description")
    private String description;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "start_datetime")
    private DateTime startDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "end_datetime")
    private DateTime endTime;

    @Column(nullable = false)
    private boolean archived = false;
}
