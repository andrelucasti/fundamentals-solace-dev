package io.andrelucas.authserver.configurations;

import com.solacesystems.jcsmp.*;
import io.andrelucas.authserver.LoginCallbackHandler;
import io.andrelucas.authserver.LoginRequestHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfiguration {

    @Bean
    public XMLMessageProducer callback(@Qualifier("session") JCSMPSession session,
                                       @Qualifier("loginCallbackHandler") LoginCallbackHandler callback){
        try {
            return session.getMessageProducer(callback);
        } catch (JCSMPException e) {
            throw new RuntimeException("Error creating consumer", e);
        }
    }

//    @Bean
//    public XMLMessageConsumer topicConsumer(@Qualifier("session") JCSMPSession session,
//                                            @Qualifier("loginRequestHandler") LoginRequestHandler handler,
//                                            @Value("${solace.request.topic}") String topicName){
//        try {
//            var messageConsumer = session.getMessageConsumer(handler);
//
//            messageConsumer.start();
//            session.addSubscription(
//                    JCSMPFactory.onlyInstance().createTopic(topicName),
//                    true
//            );
//
//            return messageConsumer;
//
//        } catch (JCSMPException e) {
//           throw new RuntimeException("Error creating consumer", e);
//        }
//    }
}
