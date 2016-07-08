package com.pyrenty.akl.domain.statistics;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "akl_statistics_purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String purchaser;

    private String product;
}
