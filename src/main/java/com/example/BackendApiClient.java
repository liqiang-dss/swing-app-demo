package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class BackendApiClient {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String baseUrl;

    public BackendApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public LoginResponse login(String userId, String password) throws IOException, InterruptedException {
        LoginRequest req = new LoginRequest(userId, password);
        String requestBody = MAPPER.writeValueAsString(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> resp = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        String body = resp.body();
        if (body == null) {
            LoginResponse lr = new LoginResponse();
            lr.result = "error";
            lr.message = "empty response";
            return lr;
        }

        try {
            return MAPPER.readValue(body, LoginResponse.class);
        } catch (Exception ex) {
            LoginResponse lr = new LoginResponse();
            lr.result = "error";
            lr.message = "invalid json: " + ex.getMessage();
            return lr;
        }
    }

    public static class LoginRequest {
        public String user_id;
        public String password;

        public LoginRequest() {
        }

        public LoginRequest(String user_id, String password) {
            this.user_id = user_id;
            this.password = password;
        }
    }

    public static class LoginResponse {
        public String result;
        public String message;

        public LoginResponse() {
        }

        public boolean isSuccess() {
            return "success".equalsIgnoreCase(result);
        }
    }
}
