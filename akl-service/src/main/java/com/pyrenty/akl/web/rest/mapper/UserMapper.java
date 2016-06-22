package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.dto.UserExtendedDTO;
import com.pyrenty.akl.dto.UserPublicDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { DateMapper.class })
public interface UserMapper {
    UserExtendedDTO userToUserExtendedDTO(User user);

    @Mapping(source = "birthdate", target = "age")
    UserPublicDTO userToUserPublicDTO(User user);
}
