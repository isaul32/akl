package fi.tite.akl.domain.statistics;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "akl_statistics_kill")
public class Kill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String killer;

    private String fallen;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "killer_position_id")
    private Position killerPos;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fallen_position_id")
    private Position fallenPos;

    private String weapon;

    private boolean headshot = false;

    private boolean teammate = false;
}
