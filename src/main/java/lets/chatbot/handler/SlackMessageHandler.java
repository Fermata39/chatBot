package lets.chatbot.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lets.chatbot.vo.RequestMessage;
import lets.chatbot.vo.ResponseMessage;
import org.springframework.web.socket.*;

public class SlackMessageHandler implements WebSocketHandler {

    private final ObjectMapper mapper;
    private final DispatcherHandler dispatcher;

    public SlackMessageHandler(DispatcherHandler messageDispatcher) {
        this.dispatcher = messageDispatcher;
        this.mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        try {
            String payload = webSocketMessage.getPayload().toString();
            System.out.println("Bot message: " + payload);
            RequestMessage requestMessage = mapper.readValue(payload, RequestMessage.class);

            if (requestMessage.ofType("message")) {
                ResponseMessage response = dispatcher.getHandler(requestMessage);
                System.out.println("[ksk] response: " + response.getText());
                session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
            }
        } catch (Exception ignored) {
            System.out.println("[ksk] Exception: " + ignored.toString());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Transport Error : " + exception);

    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
