package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Group;
import com.pyrenty.akl.repository.GroupRepository;
import com.pyrenty.akl.security.SecurityUtils;
import com.pyrenty.akl.web.rest.dto.GroupDTO;
import com.pyrenty.akl.web.rest.mapper.GroupMapper;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupResource {

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private GroupMapper groupMapper;

    @Timed
    @RequestMapping(method = RequestMethod.GET)
    public List<GroupDTO> listAll() {
        return groupRepository.findAll().stream()
                .map(o -> groupMapper.groupToGroupDTO(o))
                .collect(Collectors.toList());
    }

    @Timed
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public GroupDTO get(@PathVariable Long id) {
        return groupMapper.groupToGroupDTO(groupRepository.findOne(id));
    }

    @Timed
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method= RequestMethod.POST)
    public GroupDTO create(@RequestBody GroupDTO dto) {
        Group group = groupRepository.save(groupMapper.groupDTOToGroup(dto));
        return groupMapper.groupToGroupDTO(group);
    }

    @Timed
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public ResponseEntity<GroupDTO> update(@PathVariable Long id, @RequestBody GroupDTO dto) {
        return Optional.ofNullable(groupRepository.findOne(id))
                .map(group -> {
                    group.setName(dto.getName());
                    group.setTeams(dto.getTeams());
                    group.setLastModifiedDate(new DateTime());
                    group.setLastModifiedBy(SecurityUtils.getCurrentLogin());

                    return groupRepository.save(group);
                })
                .map(group -> groupMapper.groupToGroupDTO(group))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Timed
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        groupRepository.delete(id);
    }
}
