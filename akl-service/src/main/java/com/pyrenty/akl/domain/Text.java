package com.pyrenty.akl.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "akl_text")
@EqualsAndHashCode(callSuper = true)
public class Text extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "clob")
    private String fi;

    @Column(columnDefinition = "clob")
    private String en;
}
