package io.andrelucas.webappserver;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class SolaceCloudProxy {

    public record SolaceUserLogin(String username, String password) { }
    public record AuthResponse(boolean isAuthenticated) { }

    private final String host;
    private final String username;
    private final String password;

    public SolaceCloudProxy(@Value("${solace.host}") String host,
                            @Value("${solace.username}") String username,
                            @Value("${solace.password}") String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    private HttpHeaders httpHeaders;

    @PostConstruct
    public void init(){
        httpHeaders = new HttpHeaders() {{
            var auth = username + ":" + password;
            var encode = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            var authHeader = "Basic " + new String(encode);
            set("Authorization", authHeader);
            set("Content-Type", "application/json");
            set("Solace-Reply-Wait-Time-In-ms", "3000");
        }};
    }


    @PostMapping("/solace/cloud/proxy")
    @ResponseBody
    public ResponseEntity<Void> login(@RequestBody SolaceUserLogin user) {

        var request = new HttpEntity<>(user, httpHeaders);
        var restTemplate = new RestTemplate();
        var authResponse = restTemplate.postForObject(host + "/LOGIN/MSG/REQUEST", request, AuthResponse.class);

        if (authResponse == null || !authResponse.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
