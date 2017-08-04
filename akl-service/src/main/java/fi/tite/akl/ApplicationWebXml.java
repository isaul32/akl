package fi.tite.akl;

import fi.tite.akl.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * This is a helper Java class that provides an alternative to creating a web.xml.
 * This will be invoked only when the application is deployed to a servlet container like Tomcat, Jboss etc.
 */
@Slf4j
public class ApplicationWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.profiles(addDefaultProfile())
                .bannerMode(Banner.Mode.OFF)
                .sources(Application.class);
    }

    private String addDefaultProfile() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null) {
            log.info("Running with Spring profile(s) : {}", profile);
            return profile;
        }

        log.warn("No Spring profile configured, running with default configuration");
        return Constants.SPRING_PROFILE_DEVELOPMENT;
    }
}
