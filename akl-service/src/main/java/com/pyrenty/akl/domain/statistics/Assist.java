package com.pyrenty.akl.domain.statistics;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "akl_statistics_assist")
public class Assist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String assistant;

    private String fallen;

    private boolean teammate = false;
}
