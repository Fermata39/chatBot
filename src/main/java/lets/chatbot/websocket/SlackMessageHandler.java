package lets.chatbot.websocket;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lets.chatbot.handler.MessageDispatcher;
import lets.chatbot.vo.RequestMessage;
import lets.chatbot.vo.ResponseMessage;
import org.springframework.web.socket.*;

public class SlackMessageHandler implements WebSocketHandler {
    //    public SlackMessageHandler(Object p0) {
//    }
    ObjectMapper mapper;
    MessageDispatcher dispatcher;

    public SlackMessageHandler(MessageDispatcher messageDispatcher) {
        this.dispatcher = messageDispatcher;
        this.mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished");
    }

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {



        try {
        String payload = webSocketMessage.getPayload().toString();
        System.out.println("Bot message: " + payload);
        RequestMessage requestMessage = mapper.readValue(payload, RequestMessage.class);

        if (requestMessage.ofType("message")) {
            ResponseMessage response = dispatcher.getHandler(requestMessage);
            System.out.println("[ksk] " + response.getText());
            session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
        }
    } catch (Exception e) {
        System.out.println("[ksk] Exception: " + e.toString());
    }
}

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Exception: " + exception);
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("afterConnectionClosed: ");
    }

    public boolean supportsPartialMessages() {
        return false;
    }
}
