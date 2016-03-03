package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Text;
import com.pyrenty.akl.repository.TextRepository;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Text.
 */
@RestController
@RequestMapping("/api")
public class TextResource {

    private final Logger log = LoggerFactory.getLogger(TextResource.class);

    @Inject
    private TextRepository textRepository;


    /**
     * POST  /texts -> Create a new text.
     */
    @RequestMapping(value = "/texts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Text> create(@RequestBody Text text) throws URISyntaxException {
        log.debug("REST request to save Text : {}", text);
        if (text.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new text cannot already have an ID").body(null);
        }
        Text result = textRepository.save(text);
        return ResponseEntity.created(new URI("/api/texts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("text", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /texts -> Updates an existing text.
     */
    @RequestMapping(value = "/texts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Text> update(@RequestBody Text text) throws URISyntaxException {
        log.debug("REST request to update Text : {}", text);
        if (text.getId() == null) {
            return create(text);
        }
        Text result = textRepository.save(text);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("text", text.getId().toString()))
                .body(result);
    }

    /**
     * GET  /texts -> get all the texts.
     */
    @RequestMapping(value = "/texts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Text>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Text> page = textRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/texts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /texts/:id -> get the "id" text.
     */
    @RequestMapping(value = "/texts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Text> get(@PathVariable Long id) {
        log.debug("REST request to get Text : {}", id);
        return Optional.ofNullable(textRepository.findOne(id))
            .map(text -> new ResponseEntity<>(
                text,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /texts/:id -> delete the "id" text.
     */
    @RequestMapping(value = "/texts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Text : {}", id);
        textRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("text", id.toString())).build();
    }

}
