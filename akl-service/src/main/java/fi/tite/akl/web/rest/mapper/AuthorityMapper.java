package fi.tite.akl.web.rest.mapper;

import fi.tite.akl.domain.Authority;
import fi.tite.akl.dto.AuthorityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    AuthorityDto authorityToAuthorityDto(Authority authority);
}
