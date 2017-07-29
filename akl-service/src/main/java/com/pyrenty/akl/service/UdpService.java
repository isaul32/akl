package com.pyrenty.akl.service;

import com.pyrenty.akl.domain.statistics.*;
import com.pyrenty.akl.repository.StatisticsEventRepository;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.nio.charset.Charset;

@Service
public class UdpService {

    public enum EventType {
        KILL("killed"),
        ASSIST("assisted killing"),
        PURCHASE("purchased"),
        THROW("threw"),
        SUICIDE("committed suicide with"),
        SCORE("scored"),
        SAY("say"),
        CONNECT("connected"),
        ENTER("entered the game"),
        DISCONNECT("disconnected"),
        SWITCH("switched from team");

        private String value;

        EventType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static final EventType[] EVENT_TYPES = new EventType[] {
            EventType.KILL,
            EventType.ASSIST,
            EventType.PURCHASE,
            EventType.THROW,
            EventType.SUICIDE,
            EventType.SCORE,
            EventType.SAY,
            EventType.CONNECT,
            EventType.ENTER,
            EventType.DISCONNECT,
            EventType.SWITCH
    };

    private static final Logger log = LoggerFactory.getLogger(UdpService.class);

    @Inject
    private StatisticsEventRepository statisticsEventRepository;

    public void receive(Message message) {

        String data = new String((byte[]) message.getPayload(), Charset.forName("UTF-8"))
                .substring(5) // Some unknown chars
                .replace("\n", "")
                .replace("\r", "")
                .replaceFirst(".$",""); // Last space

        log.debug(data);

        Event event = new Event();
        event.setIp((String) message.getHeaders().get("ip_address"));

        DateTimeFormatter formatter = DateTimeFormat
                .forPattern("MM/dd/yyyy - HH:mm:ss");
        DateTime logged = formatter.parseDateTime(data.substring(2, 23));
        event.setLogged(logged);

        DateTime received = new DateTime(message.getHeaders().getTimestamp());
        event.setReceived(received);

        String line = data.substring(25);
        parse(line, event);
    }

    // https://developer.valvesoftware.com/wiki/HL_Log_Standard
    private void parse(String line, Event event) {
        boolean unknownEvent = true;

        for (EventType eventType : EVENT_TYPES) {
            String lineCmd = line.replaceAll("\".*?\"", ""); // Skip player names
            if (lineCmd.contains(eventType.toString())) {
                switch (eventType) {
                    case KILL:
                        parseKilled(line, event);
                        unknownEvent = false;
                        break;
                    case ASSIST:
                        parseAssisted(line, event);
                        unknownEvent = false;
                        break;
                    case PURCHASE:
                        parsePurchased(line, event);
                        unknownEvent = false;
                        break;
                    case THROW:
                        parseThrew(line, event);
                        unknownEvent = false;
                        break;
                    default:
                        break;
                }
            }
        }

        if (unknownEvent) {
            log.debug(line);
        } else {
            log.debug(event.toString());
            statisticsEventRepository.save(event);
        }
    }

    private void parseKilled(String line, Event event) {
        Kill kill = new Kill();

        String[] parts = line.split("\"");

        String[] killerParts = StringUtils.substringsBetween(parts[1], "<", ">");
        kill.setKiller(killerParts[1]);

        Position killerPos = new Position();
        String[] killerArray = StringUtils.substringBetween(parts[2], "[", "]").split(" ");
        killerPos.setX(Long.parseLong(killerArray[0]));
        killerPos.setY(Long.parseLong(killerArray[1]));
        killerPos.setZ(Long.parseLong(killerArray[2]));
        kill.setKillerPos(killerPos);

        String[] fallenParts = StringUtils.substringsBetween(parts[3], "<", ">");
        kill.setFallen(fallenParts[1]);

        Position fallenPos = new Position();
        String[] fallenArray = StringUtils.substringBetween(parts[4], "[", "]").split(" ");
        fallenPos.setX(Long.parseLong(fallenArray[0]));
        fallenPos.setY(Long.parseLong(fallenArray[1]));
        fallenPos.setZ(Long.parseLong(fallenArray[2]));
        kill.setFallenPos(fallenPos);

        kill.setWeapon(parts[5]);

        if (parts.length == 7 && parts[6].contains("headshot")) {
            kill.setHeadshot(true);
        }

        if (killerParts[2].equals(fallenParts[2])) {
            kill.setTeammate(true);
        }

        event.setKill(kill);
    }

    private void parseAssisted(String line, Event event) {
        Assist assist = new Assist();

        String[] parts = line.split("\"");

        String[] assistantParts = StringUtils.substringsBetween(parts[1], "<", ">");
        assist.setAssistant(assistantParts[1]);

        String[] fallenParts = StringUtils.substringsBetween(parts[3], "<", ">");
        assist.setFallen(fallenParts[1]);

        if (assistantParts[2].equals(fallenParts[2])) {
            assist.setTeammate(true);
        }

        event.setAssist(assist);
    }

    private void parsePurchased(String line, Event event) {
        Purchase purchase = new Purchase();

        String[] parts = line.split("\"");

        String[] purchaserParts = StringUtils.substringsBetween(parts[1], "<", ">");
        purchase.setPurchaser(purchaserParts[1]);

        purchase.setProduct(parts[3]);

        event.setPurchase(purchase);
    }

    private void parseThrew(String line, Event event) {
        Projectile projectile = new Projectile();

        String[] parts = line.split("\"");

        String[] purchaserParts = StringUtils.substringsBetween(parts[1], "<", ">");
        projectile.setThrower(purchaserParts[1]);

        projectile.setType(StringUtils.substringBetween(parts[2], "threw ", " ["));

        Position throwerPos = new Position();
        String[] throwerArray = StringUtils.substringBetween(parts[2], "[", "]").split(" ");
        throwerPos.setX(Long.parseLong(throwerArray[0]));
        throwerPos.setY(Long.parseLong(throwerArray[1]));
        throwerPos.setZ(Long.parseLong(throwerArray[2]));
        projectile.setThrowerPos(throwerPos);

        event.setProjectile(projectile);
    }
}
