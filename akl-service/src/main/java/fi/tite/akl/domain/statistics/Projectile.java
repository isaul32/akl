package fi.tite.akl.domain.statistics;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "akl_statistics_projectile")
public class Projectile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String thrower;

    private String type;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "thrower_position_id")
    private Position throwerPos;
}
