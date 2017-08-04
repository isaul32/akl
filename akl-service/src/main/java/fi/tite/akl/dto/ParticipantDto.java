package fi.tite.akl.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName(value = "participant")
public class ParticipantDto {
    private String name;
}
