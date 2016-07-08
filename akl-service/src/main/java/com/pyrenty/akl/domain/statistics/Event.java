package com.pyrenty.akl.domain.statistics;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Data
@Entity
@Table(name = "akl_statistics_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String ip;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime logged;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime received;

    @OneToOne(cascade = CascadeType.ALL)
    private Kill kill;

    @OneToOne(cascade = CascadeType.ALL)
    private Assist assist;

    @OneToOne(cascade = CascadeType.ALL)
    private Purchase purchase;

    @OneToOne(cascade = CascadeType.ALL)
    private Projectile projectile;
}
