package lets.chatbot.main;

import lets.chatbot.connector.ConnectorChatbot;
import lets.chatbot.handler.DispatcherHandler;
import lets.chatbot.handler.DustBotHandler;

import java.io.IOException;

/**
 * Created by shinkook.kim on 2017-06-13.
 */
public class ChatbotMain {
    public static void main(String[] args) throws IOException {

        DispatcherHandler dispatcherHandler = new DispatcherHandler();
        dispatcherHandler.addHandler("dust", new DustBotHandler());

        ConnectorChatbot conn = new ConnectorChatbot();
        conn.connector();
    }
}

