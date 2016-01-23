package com.pyrenty.akl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.pyrenty.akl.domain.enumeration.Language;

/**
 * A LocalizedText.
 */
@Entity
@Table(name = "LOCALIZEDTEXT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="localizedtext")
public class LocalizedText implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
    
    @Column(name = "text")
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalizedText localizedText = (LocalizedText) o;

        if ( ! Objects.equals(id, localizedText.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocalizedText{" +
                "id=" + id +
                ", language='" + language + "'" +
                ", text='" + text + "'" +
                '}';
    }
}
