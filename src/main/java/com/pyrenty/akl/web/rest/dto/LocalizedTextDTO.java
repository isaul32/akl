package com.pyrenty.akl.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import com.pyrenty.akl.domain.enumeration.Language;

/**
 * A DTO for the LocalizedText entity.
 */
public class LocalizedTextDTO implements Serializable {

    private Long id;

    private Language language;

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

        LocalizedTextDTO localizedTextDTO = (LocalizedTextDTO) o;

        if ( ! Objects.equals(id, localizedTextDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocalizedTextDTO{" +
                "id=" + id +
                ", language='" + language + "'" +
                ", text='" + text + "'" +
                '}';
    }
}
