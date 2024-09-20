package io.andrelucas.authserver.configurations;

import com.solacesystems.jcsmp.*;
import io.andrelucas.authserver.LoginRequestHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginQueueConfiguration {

    private final String queueName;
    private final JCSMPSession session;
    private final LoginRequestHandler handler;

    public LoginQueueConfiguration(@Value("${solace.request.queue}") String queueName,
                                   @Qualifier("session") JCSMPSession session,
                                   @Qualifier("loginRequestHandler") LoginRequestHandler handler) {
        this.queueName = queueName;
        this.session = session;
        this.handler = handler;
    }

    @PostConstruct
    public void init(){
        try {
            final EndpointProperties endpointProps = endpointProperties();
            final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);

            session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);
            System.out.println("Connected to queue: " + queue.getName());

            final ConsumerFlowProperties flowProps = consumerFlowProperties(queue);
            final FlowReceiver receiver = session.createFlow(handler, flowProps, endpointProps, (o, flowEventArgs) -> {
                System.out.println("Flow event: " + flowEventArgs.getEvent());
            });

            // Start the consumer
            receiver.start();

        } catch (JCSMPException e) {
            throw new RuntimeException("Error creating consumer", e);
        }
    }
    private EndpointProperties endpointProperties(){
        var endpointProperties = new EndpointProperties();
        endpointProperties.setPermission(EndpointProperties.PERMISSION_CONSUME);
        endpointProperties.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);

        return endpointProperties;
    }


    private ConsumerFlowProperties consumerFlowProperties(final Queue queue){
        // Create a Flow be able to bind to and consume messages from the Queue.
        var flowProperties = new ConsumerFlowProperties();
        flowProperties.setEndpoint(queue);
        flowProperties.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_AUTO);

        return flowProperties;
    }
}
