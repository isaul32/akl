package com.pyrenty.akl.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "akl_text")
public class Text implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "clob")
    private String fi;

    @Column(columnDefinition = "clob")
    private String en;
}
