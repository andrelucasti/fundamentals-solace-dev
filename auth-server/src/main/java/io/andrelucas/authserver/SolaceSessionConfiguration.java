package io.andrelucas.authserver;

import com.solacesystems.jcsmp.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceSessionConfiguration {

    private final String host;
    private final String username;
    private final String password;
    private final String vpn;

    public SolaceSessionConfiguration(@Value("${solace.host}") String host,
                                      @Value("${solace.username}") String username,
                                      @Value("${solace.password}") String password,
                                      @Value("${solace.vpn}") String vpn) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.vpn = vpn;
    }

    @Bean
    public JCSMPSession session(){
        var properties = loadProperties();
        try {
            var session = JCSMPFactory.onlyInstance().createSession(properties);
            session.connect();
            return session;
        } catch (InvalidPropertiesException e) {
            throw new RuntimeException("Error creating session", e);
        } catch (JCSMPException e) {
            throw new RuntimeException("Error connecting to session", e);
        }
    }


    private JCSMPProperties loadProperties() {
        var properties = new JCSMPProperties();

        properties.setProperty(JCSMPProperties.HOST, host);
        properties.setProperty(JCSMPProperties.USERNAME, username);
        properties.setProperty(JCSMPProperties.PASSWORD, password);
        properties.setProperty(JCSMPProperties.VPN_NAME, vpn);

        // With reapply subscriptions enabled, the API maintains a
        // cache of added subscriptions in memory. These subscriptions
        // are automatically reapplied following a channel reconnect.
        properties.setBooleanProperty(JCSMPProperties.REAPPLY_SUBSCRIPTIONS, true);
        // Disable certificate checking
        properties.setBooleanProperty(JCSMPProperties.SSL_VALIDATE_CERTIFICATE, false);

        return properties;
    }
}
