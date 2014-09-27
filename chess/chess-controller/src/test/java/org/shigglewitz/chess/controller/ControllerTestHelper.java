package org.shigglewitz.chess.controller;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class ControllerTestHelper {
	private static final boolean DEBUG = true;
	private static final String DIVIDER = "~~~~~~~~~";
	private static final String HEADER_DIVIDER = "~HEADERS~";
	private static final String BODY_DIVIDER = "~~~BODY~~";
	private static final String STATUS_DIVIDER = "~STATUS~~";

	private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();
	private static final RestTemplate REST_TEMPLATE = new RestTemplate();

	static {
		// don't throw exception on errors
		REST_TEMPLATE.setErrorHandler(new ResponseErrorHandler() {

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

		List<HttpMessageConverter<?>> msgConverters = new ArrayList<>();

		msgConverters.add(new FormHttpMessageConverter());
		msgConverters.add(new StringHttpMessageConverter());

		REST_TEMPLATE.setMessageConverters(msgConverters);
	}

	public static <T> T getEntity(String input, Class<T> clazz) {
		T ret = null;
		try {
			ret = JACKSON_MAPPER.readValue(input, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read class " + clazz + " from input: " + input);
		}

		return ret;
	}

	public static String post(URL deploymentUrl, String path,
			HttpMethod method, MultiValueMap<String, String> params) {
		String response = null;

		String url = getUrl(deploymentUrl, path);

		if (DEBUG) {
			printRequest(url, method, params);
		}

		response = REST_TEMPLATE.postForObject(url, params, String.class);
		if (DEBUG) {
			// printResponse(response);
			System.out.println("~~~~~~ " + response);
		}

		return response;
	}

	public static ResponseEntity<String> accessUrl(URL deploymentUrl,
			String path, HttpMethod method, MultiValueMap<String, String> params) {
		ResponseEntity<String> response = null;

		String url = getUrl(deploymentUrl, path);

		if (DEBUG) {
			printRequest(url, method, params);
		}

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
				params, null);

		response = REST_TEMPLATE.exchange(url, method, entity, String.class);
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

	private static void printRequest(String url, HttpMethod method,
			MultiValueMap<String, String> params) {
		log(DIVIDER);
		log("accessing URL " + url);
		log("with method " + method.toString());
		if (params != null) {
			for (Map.Entry<String, List<String>> entry : params.entrySet()) {
				log("with param " + entry.getKey() + " : "
						+ StringUtils.join(entry.getValue(), ", "));
			}
		}
		log(DIVIDER);
	}

	private static void printResponse(ResponseEntity<String> response) {
		log(DIVIDER);
		log(STATUS_DIVIDER);
		log(response.getStatusCode().value() + ": " + response.getStatusCode());
		log(HEADER_DIVIDER);
		for (String headerKey : response.getHeaders().keySet()) {
			log(headerKey + ": " + response.getHeaders().getFirst(headerKey));
		}
		log(BODY_DIVIDER);
		log(response.getBody());
		log(DIVIDER);
	}

	private static void log(String s) {
		System.out.println(s);
	}
}
