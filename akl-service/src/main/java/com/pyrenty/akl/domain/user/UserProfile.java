package com.pyrenty.akl.domain.user;

import com.pyrenty.akl.domain.enumeration.Rank;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created by Sauli on 3.3.2016.
 */

@Entity
@Table(name = "USER_PROFILE")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column( name = "image" )
    @Lob
    private byte[] image;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Min(1900)
    @Max(3000)
    private DateTime birthdate;

    private String guild;

    private Rank rank;

    @Column(columnDefinition = "TEXT", insertable = true, updatable = false)
    private String description;
}
