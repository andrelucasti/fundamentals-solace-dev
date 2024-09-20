package io.andrelucas.authserver;

import com.solacesystems.jcsmp.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfiguration {

    @Bean
    public XMLMessageConsumer consumer(@Qualifier("session") JCSMPSession session,
                                       @Qualifier("loginRequestHandler") LoginRequestHandler handler,
                                       @Value("${solace.request.topic}") String topicName){
        try {
            var messageConsumer = session.getMessageConsumer(handler);

            messageConsumer.start();
            session.addSubscription(
                    JCSMPFactory.onlyInstance().createTopic(topicName),
                    true
            );

            return messageConsumer;

        } catch (JCSMPException e) {
           throw new RuntimeException("Error creating consumer", e);
        }
    }

    @Bean
    public XMLMessageProducer callback(@Qualifier("session") JCSMPSession session){
        try {
            return session.getMessageProducer(new LoginCallback());
        } catch (JCSMPException e) {
            throw new RuntimeException("Error creating consumer", e);
        }
    }
}
