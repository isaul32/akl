package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.web.rest.dto.UserBaseDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserBaseDTO userToUserBaseDTO(User userBaseDTO);
}
