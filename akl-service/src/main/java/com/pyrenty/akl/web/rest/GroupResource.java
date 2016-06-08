package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.domain.Group;
import com.pyrenty.akl.repository.GroupRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupResource extends RESTResource<Group, Long> {

    @Inject
    public GroupResource(GroupRepository repo) {
        super(repo);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> create(@RequestBody Group json) {
        return super.create(json);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Group json) {
        return super.update(id, json);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> delete(@PathVariable Long id) {
        return super.delete(id);
    }
}
