package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.web.rest.dto.UserExtendedDTO;
import com.pyrenty.akl.web.rest.dto.UserPublicDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserExtendedDTO userToUserExtendedDTO(User user);
    UserPublicDTO userToUserPublicDTO(User user);
}
