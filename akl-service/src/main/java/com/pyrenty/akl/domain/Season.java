package com.pyrenty.akl.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

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

    @Column(name = "start_datetime")
    private Date startDate;

    @Column(name = "end_datetime")
    private Date endTime;

    @Column(nullable = false)
    private boolean archived = false;
}
