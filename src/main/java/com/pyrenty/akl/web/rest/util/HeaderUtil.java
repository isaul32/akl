package com.pyrenty.akl.web.rest.util;

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

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("aklApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("aklApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("aklApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String param, String error) {
        return createAlert("aklApp." + entityName + ".failed", param, error);
    }
}
