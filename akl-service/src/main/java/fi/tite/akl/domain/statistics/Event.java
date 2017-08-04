package fi.tite.akl.domain.statistics;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "akl_statistics_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ip;

    private LocalDateTime logged;

    private LocalDateTime received;

    @OneToOne(cascade = CascadeType.ALL)
    private Kill kill;

    @OneToOne(cascade = CascadeType.ALL)
    private Assist assist;

    @OneToOne(cascade = CascadeType.ALL)
    private Purchase purchase;

    @OneToOne(cascade = CascadeType.ALL)
    private Projectile projectile;
}
