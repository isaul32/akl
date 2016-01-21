package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetPlayerSummariesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import com.pyrenty.akl.repository.SteamCommunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class SteamCommunityResource {
    private final Logger log = LoggerFactory.getLogger(SteamCommunityResource.class);

    @Value("${akl.steam.web-api-key}")
    private String webApiKey;

    @Autowired
    private SteamCommunityRepository steamCommunityRepository;

    @RequestMapping(value = "/steam/user/{communityId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<GetPlayerSummaries> getSteamUser(@PathVariable String communityId) throws SteamApiException {
        log.debug("REST request to get Steam profile : {}", communityId);

        GetPlayerSummaries summaries = steamCommunityRepository.findSteamUser(communityId);
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

}
