package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.*;
import com.pyrenty.akl.web.rest.dto.LocalizedTextDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LocalizedText and its DTO LocalizedTextDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocalizedTextMapper {

    LocalizedTextDTO localizedTextToLocalizedTextDTO(LocalizedText localizedText);

    LocalizedText localizedTextDTOToLocalizedText(LocalizedTextDTO localizedTextDTO);
}
