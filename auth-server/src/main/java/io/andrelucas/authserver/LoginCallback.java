package io.andrelucas.authserver;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import org.springframework.stereotype.Service;


@Service
public class LoginCallback implements JCSMPStreamingPublishEventHandler {
    @Override
    public void responseReceived(String messageID) {
        System.out.println("Response received for message: " + messageID);
    }

    @Override
    public void handleError(String messageID, JCSMPException e, long l) {
        System.err.println("Error occurred for message: " + messageID);
        e.printStackTrace();
    }
}
