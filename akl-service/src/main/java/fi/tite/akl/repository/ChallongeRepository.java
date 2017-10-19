package fi.tite.akl.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fi.tite.akl.dto.ParticipantDto;
import fi.tite.akl.dto.challonge.TournamentDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Base64;

@Slf4j
@Repository
public class ChallongeRepository {

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${akl.challonge.username:}")
    private String username;

    @Value("${akl.challonge.key:}")
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

    @Cacheable(value="challonge")
    public String getScorecard(String subdomain, String tournamentUrl) throws IOException {
        Document doc = Jsoup.connect("http://" + subdomain + ".challonge.com/" + tournamentUrl).get();
        Elements scorecard = doc.select("#scorecard");
        Elements table = scorecard.select("table");

        // Sanitize
        String clean = Jsoup.clean(table.toString(), Whitelist.relaxed());
        table = Jsoup.parse(clean).body().select("table");

        table.attr("class", "table table-condensed table-striped");
        return table.toString();
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
