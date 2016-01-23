package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.LocalizedText;
import com.pyrenty.akl.repository.LocalizedTextRepository;
import com.pyrenty.akl.repository.search.LocalizedTextSearchRepository;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import com.pyrenty.akl.web.rest.dto.LocalizedTextDTO;
import com.pyrenty.akl.web.rest.mapper.LocalizedTextMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LocalizedText.
 */
@RestController
@RequestMapping("/api")
public class LocalizedTextResource {

    private final Logger log = LoggerFactory.getLogger(LocalizedTextResource.class);

    @Inject
    private LocalizedTextRepository localizedTextRepository;

    @Inject
    private LocalizedTextMapper localizedTextMapper;

    @Inject
    private LocalizedTextSearchRepository localizedTextSearchRepository;

    /**
     * POST  /localizedTexts -> Create a new localizedText.
     */
    @RequestMapping(value = "/localizedTexts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocalizedTextDTO> create(@RequestBody LocalizedTextDTO localizedTextDTO) throws URISyntaxException {
        log.debug("REST request to save LocalizedText : {}", localizedTextDTO);
        if (localizedTextDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new localizedText cannot already have an ID").body(null);
        }
        LocalizedText localizedText = localizedTextMapper.localizedTextDTOToLocalizedText(localizedTextDTO);
        LocalizedText result = localizedTextRepository.save(localizedText);
        localizedTextSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/localizedTexts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("localizedText", result.getId().toString()))
                .body(localizedTextMapper.localizedTextToLocalizedTextDTO(result));
    }

    /**
     * PUT  /localizedTexts -> Updates an existing localizedText.
     */
    @RequestMapping(value = "/localizedTexts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocalizedTextDTO> update(@RequestBody LocalizedTextDTO localizedTextDTO) throws URISyntaxException {
        log.debug("REST request to update LocalizedText : {}", localizedTextDTO);
        if (localizedTextDTO.getId() == null) {
            return create(localizedTextDTO);
        }
        LocalizedText localizedText = localizedTextMapper.localizedTextDTOToLocalizedText(localizedTextDTO);
        LocalizedText result = localizedTextRepository.save(localizedText);
        localizedTextSearchRepository.save(localizedText);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("localizedText", localizedTextDTO.getId().toString()))
                .body(localizedTextMapper.localizedTextToLocalizedTextDTO(result));
    }

    /**
     * GET  /localizedTexts -> get all the localizedTexts.
     */
    @RequestMapping(value = "/localizedTexts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<LocalizedTextDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<LocalizedText> page = localizedTextRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/localizedTexts", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(localizedTextMapper::localizedTextToLocalizedTextDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /localizedTexts/:id -> get the "id" localizedText.
     */
    @RequestMapping(value = "/localizedTexts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocalizedTextDTO> get(@PathVariable Long id) {
        log.debug("REST request to get LocalizedText : {}", id);
        return Optional.ofNullable(localizedTextRepository.findOne(id))
            .map(localizedTextMapper::localizedTextToLocalizedTextDTO)
            .map(localizedTextDTO -> new ResponseEntity<>(
                localizedTextDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /localizedTexts/:id -> delete the "id" localizedText.
     */
    @RequestMapping(value = "/localizedTexts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete LocalizedText : {}", id);
        localizedTextRepository.delete(id);
        localizedTextSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("localizedText", id.toString())).build();
    }

    /**
     * SEARCH  /_search/localizedTexts/:query -> search for the localizedText corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/localizedTexts/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LocalizedText> search(@PathVariable String query) {
        return StreamSupport
            .stream(localizedTextSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
