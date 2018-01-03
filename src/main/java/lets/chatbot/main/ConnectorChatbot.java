package lets.chatbot.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import lets.chatbot.handler.DispatcherHandler;
import lets.chatbot.handler.DustHandler;
import lets.chatbot.websocket.SlackMessageHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

import java.util.HashMap;

/**
 * Created by shinkook_mac on 2017. 6. 11..
 */

public class ConnectorChatbot {

    public static void main(String[] args) throws Exception {
        String tokenValue = "xoxb-196719547462-lhlsUMuhTdG39cZU8ZSXcSbw";
        NameValuePair formatParam = new BasicNameValuePair("token", tokenValue);

        Response response = Request.Post("https://slack.com/api/rtm.connect")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .bodyForm(formatParam)
                .execute();

        ObjectMapper om = new ObjectMapper();
        HashMap returnValue = om.readValue(response.returnContent().asBytes(), HashMap.class);
        String url = (String) returnValue.get("url");


        DispatcherHandler dispatcher = new DispatcherHandler();
        dispatcher.addHandler("먼지", new DustHandler());

        System.out.println("[ksk] url: " + url);

        //webSocketClient 를 통해 보안으로 client 를 생성
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(true);
        org.eclipse.jetty.websocket.client.WebSocketClient webSocketClient = new org.eclipse.jetty.websocket.client.WebSocketClient(
                sslContextFactory);
        WebSocketClient client = new JettyWebSocketClient(webSocketClient);
        WebSocketHandler handler = new SlackMessageHandler(dispatcher);

        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(client,new ExceptionWebSocketHandlerDecorator(handler),url);
        connectionManager.start();
    }
}
