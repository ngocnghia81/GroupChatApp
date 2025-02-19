package com.tripleng.RealTime.Chat.App.config;

import com.tripleng.RealTime.Chat.App.chat.ChatMessage;
import com.tripleng.RealTime.Chat.App.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleDisconnectEventListener(
        SessionDisconnectEvent disconnectEvent
    ){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String username = headerAccessor.getSessionAttributes().get("username").toString();
        if(username!=null){
            log.info("User disconnected: {}",username);
            var chatMessage  = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messagingTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
