package com.it5240.sportfriend.utils;

import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.UserRepository;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class NotificationUtil {

    @Autowired
    private UserRepository userRepository;

    public void sendNotificationToUserId(String userId, String title, String message){
        try {
            User user = userRepository.findById(userId).get();
            sendNotificationToUser(user, title, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationToUser(User user, String title, String message){
        if(user.getPushToken() == null) return;
        sendMessage(user.getPushToken(), title, message);
    }

    public void sendMessage(String recipient, String title, String message) {
        try {
            if (!PushClient.isExponentPushToken(recipient))
                return;

            ExpoPushMessage expoPushMessage = new ExpoPushMessage();
            expoPushMessage.getTo().add(recipient);
            expoPushMessage.setTitle(title);
            expoPushMessage.setBody(message);

            List<ExpoPushMessage> expoPushMessages = new ArrayList<>();
            expoPushMessages.add(expoPushMessage);

            PushClient client = new PushClient();
            List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(expoPushMessages);

            List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();

            for (List<ExpoPushMessage> chunk : chunks) {
                messageRepliesFutures.add(client.sendPushNotificationsAsync(chunk));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
