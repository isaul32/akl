package com.pyrenty.akl.service;

import com.pyrenty.akl.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service class for managing steam users.
 */
@Service
public class SteamUserService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    private final Logger log = LoggerFactory.getLogger(SteamUserService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        if (token != null && token.getName() != null) {
            String[] parts = token.getName().split("/");
            if (parts.length == 6) {
                String domain = parts[2];
                System.out.println("domain: " + domain);
                if (domain.equals("steamcommunity.com")) {
                    try {
                        long communityId = Long.parseUnsignedLong(parts[5]);
                        String steamId = convertCommunityIdToSteamId(communityId);

                        System.out.println("communityId: " + communityId);
                        System.out.println("steamId: " + steamId);

                        /*User user = new User();
                        user.setCommunityId(communityId);
                        user.setSteamId(steamId);
                        user.setActivated(true);
                        user.setLogin("test");
                        user.setPassword("test");

                        userService.createUser(user);*/


                        //todo: migration script

                        //return new User(token.getName(), "", AuthorityUtils.createAuthorityList("ROLE_USER"));
                    } catch (NumberFormatException ex) {
                        log.error(ex.getLocalizedMessage());
                    }
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
