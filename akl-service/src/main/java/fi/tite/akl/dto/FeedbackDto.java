package fi.tite.akl.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class FeedbackDto {
    @NotNull
    @Length(min = 10, max = 1000)
    private String message;

    @NotNull
    @Length(min = 5, max = 50)
    private String sender;

    @NotNull
    private String recaptcha;
}
