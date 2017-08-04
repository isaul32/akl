package fi.tite.akl.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-aklApp-alert", message);
        headers.add("X-aklApp-params", param);
        return headers;
    }

    public static HttpHeaders createAlert(String message, String param, String error) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-aklApp-alert", message);
        headers.add("X-aklApp-params", param);
        headers.add("X-aklApp-error", error);
        return headers;
    }
}
