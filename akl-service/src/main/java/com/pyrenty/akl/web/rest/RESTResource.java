package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class RESTResource<T, ID extends Serializable> {
    private CrudRepository<T, ID> repo;

    public RESTResource(CrudRepository<T, ID> repo) {
        this.repo = repo;
    }

    @Timed
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<T> listAll() {
        Iterable<T> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Timed
    @RequestMapping(method= RequestMethod.POST)
    public @ResponseBody Map<String, Object> create(@RequestBody T json) {
        T created = this.repo.save(json);

        Map<String, Object> m = Maps.newHashMap();
        m.put("success", true);
        m.put("created", created);
        return m;
    }

    @Timed
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public @ResponseBody T get(@PathVariable ID id) {
        return this.repo.findOne(id);
    }

    @Timed
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public @ResponseBody Map<String, Object> update(@PathVariable ID id, @RequestBody T json) {
        T entity = this.repo.findOne(id);
        try {
            BeanUtils.copyProperties(entity, json);
        }
        catch (Exception ex) {
            throw Throwables.propagate(ex);
        }

        T updated = this.repo.save(entity);

        Map<String, Object> m = Maps.newHashMap();
        m.put("success", true);
        m.put("id", id);
        m.put("updated", updated);
        return m;
    }

    @Timed
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public @ResponseBody Map<String, Object> delete(@PathVariable ID id) {
        this.repo.delete(id);
        Map<String, Object> m = Maps.newHashMap();
        m.put("success", true);
        return m;
    }
}
