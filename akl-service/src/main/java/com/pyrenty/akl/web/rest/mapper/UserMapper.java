package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.dto.UserExtendedDto;
import com.pyrenty.akl.dto.UserPublicDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { DateMapper.class })
public interface UserMapper {
    UserExtendedDto userToUserExtendedDto(User user);

    @Mapping(source = "birthdate", target = "age")
    UserPublicDto userToUserPublicDto(User user);
}
