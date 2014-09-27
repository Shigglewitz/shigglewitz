package org.shigglewitz.chess.controller;

import java.io.IOException;
import java.net.URL;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class ControllerTestHelper {
    private static final boolean DEBUG = true;

    public static ResponseEntity<String> accessUrl(URL deploymentUrl,
            String path, HttpMethod method) {
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();

        // don't throw exception on errors
        restTemplate.setErrorHandler(new ResponseErrorHandler() {

            @Override
            public boolean hasError(ClientHttpResponse response)
                    throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response)
                    throws IOException {
                return;
            }

        });

        String url = getUrl(deploymentUrl, path);

        log("accessing URL " + url + " with method " + method.toString());
        response = restTemplate.exchange(url, method, null, String.class);
        if (DEBUG) {
            printResponse(response);
        }

        return response;
    }

    private static String getUrl(URL deploymentUrl, String path) {
        // remove the last / from the deployment url
        // the $ is the regex symbol for end of line
        return deploymentUrl.toString().replaceAll("/$", "") + path;
    }

    private static void printResponse(ResponseEntity<String> response) {
        String divider = "~~~~~~~~~";
        String headerDivider = "~HEADERS~";
        String bodyDivider = "~~~BODY~~";
        String statusDivider = "~STATUS~~";

        log(divider);
        log(statusDivider);
        log(response.getStatusCode().value() + ": " + response.getStatusCode());
        log(headerDivider);
        for (String headerKey : response.getHeaders().keySet()) {
            log(headerKey + ": " + response.getHeaders().getFirst(headerKey));
        }
        log(bodyDivider);
        log(response.getBody());
        log(divider);
    }

    private static void log(String s) {
        System.out.println(s);
    }
}
