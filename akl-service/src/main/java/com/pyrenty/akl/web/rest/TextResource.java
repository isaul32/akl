package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Text;
import com.pyrenty.akl.service.TextService;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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
    private TextService textService;

    @RequestMapping(value = "/texts", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<Text>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                             @RequestParam(value = "per_page", required = false) Integer limit)
            throws URISyntaxException {
        log.debug("REST request to get all Texts");

        Page<Text> page = textService.getAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/texts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/texts/{id}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<Text> get(@PathVariable Long id) {
        log.debug("REST request to get Text : {}", id);

        return textService.get(id)
            .map(text -> new ResponseEntity<>(text, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/texts/{id}", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<Text> update(@PathVariable Long id,
                                       @RequestBody Text text) throws URISyntaxException {
        log.debug("REST request to update Text : {}", id);

        return Optional.ofNullable(textService.update(id, text))
                .map(newText -> ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert("text", id.toString()))
                        .body(newText))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
