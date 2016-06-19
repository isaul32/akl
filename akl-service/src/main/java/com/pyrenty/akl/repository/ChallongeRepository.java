package com.pyrenty.akl.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pyrenty.akl.web.rest.dto.ParticipantDto;
import com.pyrenty.akl.web.rest.dto.TournamentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Base64;

@Repository
public class ChallongeRepository {

    private final Logger log = LoggerFactory.getLogger(ChallongeRepository.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${akl.challonge.username}")
    private String username;

    @Value("${akl.challonge.key}")
    private String challongeKey;

    public ChallongeRepository() {
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
    }

    public void getTournament(TournamentDto dto) throws IOException {

    }

    public boolean createTournament(TournamentDto dto) throws IOException {
        return openConnection("https://api.challonge.com/v1/tournaments.json", "POST", mapper.writeValueAsString(dto));
    }

    public boolean createParticipant(TournamentDto tournamentDto, ParticipantDto participantDto) throws IOException {
        return openConnection("https://api.challonge.com/v1/tournaments/"
                + tournamentDto.getSubdomain() + "-" + tournamentDto.getUrl()
                + "/participants.json", "POST", mapper.writeValueAsString(participantDto));
    }

    public boolean startTournament(TournamentDto tournamentDto) throws IOException {
        return openConnection("https://api.challonge.com/v1/tournaments/"
                + tournamentDto.getSubdomain() + "-" + tournamentDto.getUrl()
                + "/start.json", "POST", "");
    }

    public boolean deleteTournament(TournamentDto tournamentDto) throws IOException {
        return openConnection("https://api.challonge.com/v1/tournaments/"
                + tournamentDto.getSubdomain() + "-" + tournamentDto.getUrl()
                + ".json", "DELETE", "");
    }

    public boolean deleteAllTournaments(String subdomain, String tournamentUrl) {
        return false;
    }

    private boolean openConnection(String spec, String method, String data) throws IOException {
        URL url = new URL(spec);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);

        // Request header
        connection.setRequestMethod(method);
        connection.setRequestProperty("Authorization", buildBasicAuthorizationString(username, challongeKey));
        connection.setRequestProperty("Content-Type", "application/json");

        // Request body
        log.debug(data);
        DataOutputStream os = new DataOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(data);
        writer.flush();
        writer.close();

        log.debug(connection.getResponseCode() + " " + connection.getResponseMessage());

        // Response
        BufferedReader reader;
        InputStream is;
        boolean success;

        if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            is = connection.getInputStream();
            success = true;
        } else {
            is = connection.getErrorStream();
            success = false;
        }

        if (is != null) {
            reader = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            if (success) {
                log.debug(response.toString());
            } else {
                log.error(response.toString());
            }

            return success;
        }

        return false;
    }


    private static String buildBasicAuthorizationString(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
