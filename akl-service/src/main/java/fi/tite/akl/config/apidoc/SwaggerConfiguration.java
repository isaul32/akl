package fi.tite.akl.config.apidoc;

import fi.tite.akl.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static springfox.documentation.builders.PathSelectors.regex;

@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements EnvironmentAware {

    @Inject
    private Environment env;

    @Value("${akl.prod.host:''}")
    private String prodHost;

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
    }

    /**
     * Swagger Springfox configuration.
     */
    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .directModelSubstitute(java.time.LocalDate.class, String.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();

        // Fix host in production
        if (env.getActiveProfiles().length > 0) {
            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
            if (activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
                docket.host(prodHost);
            }
        }

        watch.stop();

        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());

        return docket;
    }

    /**
     * API Info as it appears on the swagger-ui page.
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact(propertyResolver.getProperty("contact.name"),
                propertyResolver.getProperty("contact.url"),
                propertyResolver.getProperty("contact.email"));
        Collection<VendorExtension> extensions = new ArrayList<>();

        return new ApiInfo(
                propertyResolver.getProperty("title"),
                propertyResolver.getProperty("description"),
                propertyResolver.getProperty("version"),
                propertyResolver.getProperty("termsOfServiceUrl"),
                contact,
                propertyResolver.getProperty("license"),
                propertyResolver.getProperty("licenseUrl"),
                extensions);
    }
}
