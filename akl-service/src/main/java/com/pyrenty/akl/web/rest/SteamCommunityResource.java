package com.pyrenty.akl.web.rest;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.pyrenty.akl.repository.SteamCommunityRepository;
import com.pyrenty.akl.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@Slf4j
@RestController
@RequestMapping("/api/steam")
public class SteamCommunityResource {

    @Value("${akl.steam.web-api-key:}")
    private String webApiKey;

    @Inject
    private SteamCommunityRepository steamCommunityRepository;

    @Inject
    private UserRepository userRepository;

    @RequestMapping(value = "/user/{communityId}", method = RequestMethod.GET)
    ResponseEntity<GetPlayerSummaries> getSteamUser(@PathVariable String communityId) throws SteamApiException {
        log.debug("REST request to get Steam profile : {}", communityId);

        return userRepository.findOneByCommunityId(communityId)
                .map(u -> {
                    GetPlayerSummaries summaries = null;
                    try {
                        summaries = steamCommunityRepository.findSteamUser(communityId);
                    } catch (SteamApiException e) {
                        return new ResponseEntity<GetPlayerSummaries>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    return new ResponseEntity<>(summaries, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
