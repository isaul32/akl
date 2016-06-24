package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.*;

import com.pyrenty.akl.dto.GroupDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface GroupMapper {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Group groupDtoToGroup(GroupDto groupDto);

    GroupDto groupToGroupDto(Group group);
}
