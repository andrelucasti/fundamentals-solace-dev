package io.andrelucas.authserver;

import com.google.gson.Gson;
import com.solacesystems.jcsmp.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginRequestHandler implements XMLMessageListener {
    private final Gson gson = new Gson();
    private final XMLMessageProducer callback;
    private final IUserRepository userRepository;

    public LoginRequestHandler(XMLMessageProducer callback,
                               IUserRepository userRepository) {
        this.callback = callback;
        this.userRepository = userRepository;
    }

    @Override
    public void onReceive(BytesXMLMessage bytesXMLMessage) {
        System.out.println("Received a login request message, trying to parse it");

        if (Objects.requireNonNull(bytesXMLMessage) instanceof TextMessage message) {
            try {
                XMLMessage replyMessage = createReplyMessage(message);
                callback.sendReply(message, replyMessage);

            } catch (JCSMPException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("Failed to parse the request message, here's a message dump: " + bytesXMLMessage);
        }

    }

    @Override
    public void onException(JCSMPException e) {
        System.out.println("Error occurred for message: ");
        e.printStackTrace();
    }

    public record LoginRequest(String username, String password) { }
    private XMLMessage createReplyMessage(final TextMessage request) throws JCSMPException {
        var messageReply = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        var loginRequest = gson.fromJson(request.getText(), LoginRequest.class);

        System.out.println("Received login request for user: " + loginRequest.username());

        var validUser = isValidUser(loginRequest.username(), loginRequest.password());
        if (validUser.isAuthenticated()) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }

        messageReply.setHTTPContentType("application/json");
        messageReply.setText(gson.toJson(validUser));
        messageReply.setApplicationMessageId(request.getApplicationMessageId());
        messageReply.setDeliverToOne(true);
        messageReply.setDeliveryMode(DeliveryMode.DIRECT);

        return messageReply;
    }

    private AuthResponse isValidUser(String username, String password) {

        var optionalAuthorizedUser = userRepository.findByNameAndPassword(username, password);
        if (optionalAuthorizedUser.isEmpty()){
            return AuthResponse.unauthorized();
        }

        return AuthResponse.authorized();
    }
}
