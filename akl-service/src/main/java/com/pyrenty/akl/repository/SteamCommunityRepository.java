package com.pyrenty.akl.repository;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetPlayerSummariesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class SteamCommunityRepository {

    @Value("${akl.steam.web-api-key}")
    private String webApiKey;

    @Cacheable(value="steamUser", key="#communityId")
    public GetPlayerSummaries findSteamUser(String communityId) throws SteamApiException {

        ArrayList<String> steamIds = new ArrayList<>();
        steamIds.add(communityId);

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(webApiKey).build();
        GetPlayerSummariesRequest request = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(steamIds);

        return client.processRequest(request);
    }
}
