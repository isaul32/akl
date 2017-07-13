package com.pyrenty.akl.repository;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playerstats.GetUserStatsForGame;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetPlayerSummariesRequest;
import com.lukaspradel.steamapi.webapi.request.GetUserStatsForGameRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class SteamCommunityRepository {

    @Value("${akl.steam.web-api-key:''}")
    private String webApiKey;

    @Cacheable(value="steamUser", key="#communityId")
    public GetPlayerSummaries findSteamUser(String communityId) throws SteamApiException {

        ArrayList<String> steamIds = new ArrayList<>();
        steamIds.add(communityId);
        GetPlayerSummariesRequest request = SteamWebApiRequestFactory.createGetPlayerSummariesRequest(steamIds);

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(webApiKey).build();

        return client.processRequest(request);
    }

    @Cacheable(value="steamUserStats", key="#communityId")
    public GetUserStatsForGame findSteamUserStats(String communityId) throws SteamApiException {

        GetUserStatsForGameRequest request = SteamWebApiRequestFactory.createGetUserStatsForGameRequest(730, communityId);

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(webApiKey).build();

        return client.processRequest(request);
    }
}
