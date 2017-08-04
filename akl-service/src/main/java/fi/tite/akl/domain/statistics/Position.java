package fi.tite.akl.domain.statistics;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "akl_statistics_position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long x;

    private Long y;

    private Long z;
}
