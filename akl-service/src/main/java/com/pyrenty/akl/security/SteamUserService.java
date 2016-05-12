package com.pyrenty.akl.security;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.repository.SteamCommunityRepository;
import com.pyrenty.akl.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing steam users.
 */
@Service
public class SteamUserService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    private final Logger log = LoggerFactory.getLogger(SteamUserService.class);

    @Inject
    private UserService userService;

    @Inject
    private SteamCommunityRepository steamCommunityRepository;

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        if (token != null && token.getName() != null) {
            String[] parts = token.getName().split("/");
            if (parts.length == 6) {
                String domain = parts[2];
                if (domain.equals("steamcommunity.com")) try {
                    String communityId = parts[5];
                    String steamId = convertCommunityIdToSteamId(
                            Long.parseUnsignedLong(parts[5]));

                    User user = userService.getUserWithAuthorities(communityId);

                    if (user != null) {
                        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                                .collect(Collectors.toList());

                        return new org.springframework.security.core.userdetails
                                .User(user.getLogin(), "", authorities);
                    }

                    GetPlayerSummaries summaries = steamCommunityRepository.findSteamUser(communityId);

                    Optional<Player> player = summaries.getResponse().getPlayers().stream().findFirst();

                    if (player.isPresent()) {
                        user = userService.createSteamLoginUser(communityId, steamId,
                                player.get().getPersonaname());
                    } else {
                        user = userService.createSteamLoginUser(communityId, steamId, null);
                    }

                    return new org.springframework.security.core.userdetails
                            .User(user.getLogin(), "", AuthorityUtils.createAuthorityList("ROLE_USER"));
                } catch (SteamApiException | NumberFormatException ex) {
                    log.error(ex.getLocalizedMessage());
                }
            }
        }

        throw new UsernameNotFoundException("Steam authentication failed");
    }

    public static String convertCommunityIdToSteamId(long communityId) throws UsernameNotFoundException {
        long steamId1 = communityId % 2;
        long steamId2 = communityId - 76561197960265728L;

        if(steamId2 <= 0) {
            throw new UsernameNotFoundException("SteamId " + communityId + " is too small.");
        }

        steamId2 = (steamId2 - steamId1) / 2;

        return "STEAM_0:" + steamId1 + ":" + steamId2;
    }
}
