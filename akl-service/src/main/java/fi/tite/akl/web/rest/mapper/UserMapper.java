package fi.tite.akl.web.rest.mapper;

import fi.tite.akl.domain.User;
import fi.tite.akl.dto.UserDto;
import fi.tite.akl.dto.UserExtendedDto;
import fi.tite.akl.dto.UserPublicDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { DateMapper.class, AuthorityMapper.class })
public interface UserMapper {
    UserExtendedDto userToUserExtendedDto(User user);

    @Mapping(source = "birthdate", target = "age")
    UserPublicDto userToUserPublicDto(User user);

    @Mapping(target = "requested", ignore = true)
    UserDto userToUserDto(User user);
}
