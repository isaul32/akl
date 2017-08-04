package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.domain.Group;
import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.repository.ChallongeRepository;
import com.pyrenty.akl.repository.GroupRepository;
import com.pyrenty.akl.security.SecurityUtils;
import com.pyrenty.akl.dto.GroupDto;
import com.pyrenty.akl.dto.ParticipantDto;
import com.pyrenty.akl.dto.challonge.TournamentDto;
import com.pyrenty.akl.web.rest.mapper.GroupMapper;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupResource {

    @Value("${akl.challonge.key:}")
    private String challongeKey;

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private GroupMapper groupMapper;

    @Inject
    private ChallongeRepository challongeRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<GroupDto> listAll() {
        return groupRepository.findAll(PaginationUtil.generatePageRequest(null, null, new Sort(
                new Sort.Order(Sort.Direction.ASC, "id")
        ))).getContent().stream()
                .map(o -> groupMapper.groupToGroupDto(o))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public GroupDto get(@PathVariable Long id) {
        return groupMapper.groupToGroupDto(groupRepository.findOne(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public GroupDto create(@RequestBody GroupDto dto) {
        dto.setSubdomain("akl");
        Group group = groupRepository.save(groupMapper.groupDtoToGroup(dto));
        return groupMapper.groupToGroupDto(group);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GroupDto> update(@PathVariable Long id, @RequestBody GroupDto dto) {
        return Optional.ofNullable(groupRepository.findOne(id))
                .map(group -> {
                    group.setName(dto.getName());
                    group.setUrl(dto.getUrl());
                    group.setTeams(dto.getTeams());
                    group.setLastModifiedDate(LocalDateTime.now());
                    group.setLastModifiedBy(SecurityUtils.getCurrentLogin());

                    return groupRepository.save(group);
                })
                .map(group -> groupMapper.groupToGroupDto(group))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        groupRepository.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/tournament", method = RequestMethod.POST)
    public void createTournament() throws IOException {
        for (Group group : groupRepository.findAll()) {
            TournamentDto tournamentDto = new TournamentDto();

            tournamentDto.setName(group.getName());
            tournamentDto.setTournament_type("round robin");
            tournamentDto.setUrl(group.getUrl());
            tournamentDto.setSubdomain(group.getSubdomain());

            if (challongeRepository.createTournament(tournamentDto)) {
                for (Team team : group.getTeams()) {
                    ParticipantDto participantDto = new ParticipantDto();
                    participantDto.setName(team.getName());
                    if (!challongeRepository.createParticipant(tournamentDto, participantDto)) {
                        challongeRepository.deleteTournament(tournamentDto);
                        break;
                    }
                }

                challongeRepository.startTournament(tournamentDto);
            }
        }
    }
}
